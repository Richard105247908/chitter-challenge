# Chitter Challenge (Java port)

Build a small Twitter clone where users post messages ("peeps") to a public stream.
Work on your own, test-first. If you use anyone else's solution, link it in your README.
Commit partial solutions too.

## Features

STRAIGHT UP

As a Maker
So that I can let people know what I am doing
I want to post a message (peep) to chitter

As a Maker
So that I can see what others are saying
I want to see all peeps in reverse chronological order

As a Maker
So that I can better appreciate the context of a peep
I want to see the time at which it was made

As a Maker
So that I can post messages on Chitter as me
I want to sign up for Chitter

HARDER

As a Maker
So that only I can post messages on Chitter as me
I want to log in to Chitter

As a Maker
So that I can avoid others posting messages on Chitter as me
I want to log out of Chitter

ADVANCED

As a Maker
So that I can stay constantly tapped in to the shouty box of Chitter
I want to receive an email if I am tagged in a Peep

## Technical approach

Java 21, Maven, JUnit 5, Mockito, Spring Boot (Spring MVC + Thymeleaf), a SQL database.

| Original (Ruby)             | Java port                                              |
|-----------------------------|--------------------------------------------------------|
| Sinatra routes              | Spring MVC `@Controller`                               |
| ERB views                   | Thymeleaf templates                                    |
| `pg` gem + Repository class | `JdbcTemplate` repositories (or Spring Data JPA later) |
| RSpec                       | JUnit 5 + Mockito                                      |
| Capybara feature tests      | `@WebMvcTest` + MockMvc                                |
| SimpleCov                   | JaCoCo                                                 |
| ActiveRecord/ROM stretch    | Spring Data JPA / Hibernate                            |
| Heroku deploy               | optional, skip for now                                 |

Test databases: H2 in-memory or Testcontainers/PostgreSQL. Plan your pages first
(sign up, log in, peep stream, new peep) before you write code.

## Notes on functionality

- You don't have to be logged in to see peeps.
- Makers sign up with email, password, name and username
  (e.g. samm@makersacademy.com, password123, Sam Morgan, sjmog).
- Username and email are unique.
- Peeps show the maker's name and handle, plus the time they were posted.
- Never store plain-text passwords. Hash with BCrypt (`spring-security-crypto`, no need
  for full Spring Security).
- Keep login state in the session, like RPS.
- For the tag-email story, put a `MailSender` interface behind a seam (like `SmsSender`
  in Takeaway) and fake it in tests.
- README must list the technologies and how to install and run the tests.

## Bonus

- Reply to a peep from another maker.
- Make it look good with CSS.

## Code review

- All tests passing
- Coverage above 95% (JaCoCo)
- Elegant code: clear class responsibilities, short methods
- Rubric: replace docs/review.md with your Java checklist