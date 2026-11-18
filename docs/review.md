# Chitter Challenge (Java) - Code Review Checklist

A scaffold for reviewing your own or someone else's Chitter. These are common things to
look for. You may decide to take your own route.

# Step 0: Checkout and run tests

- `git clone`, then `./mvnw verify`. Do all tests pass from a clean checkout?
- Run the app (`./mvnw spring-boot:run`) and use it in the browser.
- Experiment with the domain classes in `jshell` (the Java version of IRB).
- How easy is the structure to follow? Did you need any cognitive leaps to "get it"?

# Step 1: How far did they get?

* Features
    * [ ] Sign up
    * [ ] Log in
    * [ ] Log out
    * [ ] Peeping
    * [ ] Listing peeps (reverse chronological)
    * [ ] Display time of peep
* Bonus
    * [ ] Replying to peeps
    * [ ] Email on tag (behind a `MailSender` seam, faked in tests)
    * [ ] CSS styling

# Step 2: Structure and supporting files

## Installation instructions in the README
- Clone, DB setup, `./mvnw test`, `./mvnw spring-boot:run`, and what port it's on.
- Technologies listed. Screenshots of the app working.
- Which database is needed, and how to create it (or does it use H2 / Testcontainers?).

## Remove unnecessary components
- Dead code in comments, unused imports, unused classes
- Files that shouldn't be committed: `target/`, `.idea/`, `*.iml`, local config with
  credentials. Check `.gitignore`. Remove already-committed files with `git rm -r --cached`.
- Consistent formatting and indentation (let IntelliJ reformat before you commit)

## pom.xml uses correct scopes
- Test libraries (`spring-boot-starter-test`, Mockito, Testcontainers, H2) have
  `<scope>test</scope>`.
- No unused dependencies. Java version and Spring Boot version are pinned.

## No secrets in the repo
- No DB passwords or mail credentials in `application.properties`. Use environment
  variables or a git-ignored local profile.

## Test configuration is sensible
- Feature tests boot the real app (`@SpringBootTest`), not hand-wired pieces that could
  hide a missing bean.
- Slice tests (`@WebMvcTest`, `@DataJpaTest` or `@JdbcTest`) are used deliberately, for speed.
- Test config doesn't copy production wiring by hand.

## Clean the database between tests
Each test should start on a known state, create its own data, and not depend on other tests.
- `@Transactional` on tests (rolls back automatically), or
- `@Sql` scripts to truncate tables, or `@DirtiesContext` (slow, use sparingly), or
- a fresh Testcontainers database per class
- Tests pass in any order and when run singly.

## Schema is managed, not manual
- Schema lives in `schema.sql` or Flyway migrations, not typed into psql by hand.
- Test and dev databases are set up by the same scripts.
- (Nice to have, like Rake in Ruby): a seed script for dummy dev data.

## Static assets in the right place
- CSS and images in `src/main/resources/static/`, referenced with `th:href="@{/css/style.css}"`,
  not relative `../../` paths.

# Step 3: Tests

## Feature tests look like feature tests
Feature tests are longer, procedural, and named for what the user does (`PostingPeepFeatureTest`,
not `UserTest`). They may expect more than one thing.

Good:
@Test
void signedInUserCanPostPeepAndSeeItInTheStream() throws Exception {
signUpAndLogIn(session);
mockMvc.perform(post("/peeps").session(session).param("message", "Hello"))
.andExpect(redirectedUrl("/peeps"));
mockMvc.perform(get("/peeps"))
.andExpect(status().isOk())
.andExpect(content().string(containsString("Hello")));
}

Not so good: three separate tests for the redirect, the status and the content of one journey.

## Correct location of feature and unit tests
- Feature tests live in their own package (e.g. `feature/`) and go through HTTP, not by
  calling services directly.
- Unit tests live in packages that mirror `src/main/java`, and fake collaborators with Mockito.
- Setting up data directly in the DB is fine, but beware states the web UI could never produce.

## Use test helpers appropriately
- Shared set-up (`signUp(...)`, `logIn(...)`, a `UserBuilder`) lives in a test utility class
  or builder, not copy-pasted.
- Don't pull production helper code into test config.

## Test quality
- Each test fails for the right reason (you watched it fail first).
- Mockito used for real boundaries (mailer, clock), not for everything.
- Time is injected (`java.time.Clock`) so ordering and timestamp tests are deterministic.
- Edge cases covered: duplicate username or email, blank peep, wrong password, logged-out
  user posting.
- JaCoCo coverage above 95% (`./mvnw verify`).

# Step 4: Application code

## Controllers
- **Thin.** Controllers parse input, call a service or repository, and choose a view. Business
  logic lives in classes (`UserService`, `PeepService`), not in controller methods.
- **Split by resource:** `PeepController`, `UserController`, `SessionController`, not one
  giant controller.
- **RESTful routes:**
  GET  /peeps          list
  GET  /peeps/new      new peep form
  POST /peeps          create
  GET  /users/new      sign-up form
  POST /users          create user
  GET  /sessions/new   log-in form
  POST /sessions       log in
  POST /sessions/delete   log out
- **Flash messages:** use `RedirectAttributes.addFlashAttribute` for messages shown after a
  redirect, and `Model` for messages shown immediately. Prefer `error` / `notice` keys over one
  key per message.
- **Post-Redirect-Get:** successful POSTs redirect, they don't render directly.
- **Constructor injection**, not `@Autowired` fields. Dependencies are `final`.
- **Don't expose entities blindly** to views or forms. Use small form objects or records
  (e.g. `record SignUpForm(String email, String password, String name, String username)`)
  with Bean Validation annotations (`@NotBlank`, `@Email`).
- **Current user:** put it in one place (a `@ControllerAdvice` with `@ModelAttribute`, or a
  small session helper), not repeated in every controller.

## Domain and persistence
- Every class has one clear responsibility. Methods are short.
- Repositories do persistence only. Hashing, validation rules and tag parsing live elsewhere.
- Passwords are hashed with BCrypt and never logged or stored in plain text.
- Uniqueness of username and email is enforced in the DB (unique constraint) AND handled
  gracefully in the UI (friendly error, not a stack trace).
- Use `Optional` for "might not exist" returns. No returning `null` from public methods.
- Prefer `Instant` / `OffsetDateTime` for peep timestamps. Format them in the view.

## Views (Thymeleaf)
- Correct form semantics: `<label for="message">` matched to `<input id="message">`.
- Use semantic HTML (`<article class="peep">`, `<main>`, `<header>`, `<nav>`) over bare `<div>`s.
- A shared layout and fragments (header, flash messages) are used where they genuinely avoid
  repetition. A fragment for a favicon is overkill.
- Proper top-level structure: `<html>`, `<head>` with `<title>`, `<body>`.
- Thymeleaf escapes output by default. Check you haven't used `th:utext` on user input.
- No logic-heavy Java in templates. Compute it in the controller or service.