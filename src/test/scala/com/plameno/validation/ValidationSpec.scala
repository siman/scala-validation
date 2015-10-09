package com.plameno.validation

import org.scalatest._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

/**
 * @author Alex Siman [https://github.com/siman]
 * @date 2013-09-27
 */

@RunWith(classOf[JUnitRunner])
class ValidationSpec extends FlatSpec {
  import ValidationSpecData._

  "Validation" should "validate all form" in {
    assert(personForm.validate === formErrors)
  }

  it should "validate a single field" in {
    assert(personForm.validateField("email") === Some(ERR_STR_REQ))
  }
  
  it should "not validate an unknown field" in {
    assert(personForm.validateField("xxx") === None)
  }
  
  it should "validate a group of fields" in {
    assert(personForm.validateFields(List("login", "age")) === loginAndAgeErrors)
  }
  
  it should "not validate an empty group of fields" in {
    assert(personForm.validateFields(Nil) === Map.empty)
  }

  it should "validate with regular expressions" in {
    assert(RegexValidator("hello world!", "^hello".r).isValid)
    val errorMsg = Some("No x marks the spot!")
    assert(RegexValidator("beep", "x".r, errorMsg).validate == errorMsg)
  }

  it should "validate any supplied function in the InCaseValidator" in {
    assert(InCaseValidator(2, (x : Int) => {x % 2 == 0}).isValid)
  }

}

object ValidationSpecData {
  
  val ERR_STR_REQ = "Field is required"
  val ERR_LOGIN_LONG = "Login is too long"
  val ERR_EMAIL_FORMAT = "Email is of invalid format"
  val ERR_AGE_POS_INT = "Age should be a positive integer"
  val ERR_AGE_OVER_18 = "You should be over 18 years old"
  
  case class Person(
    login: String,
    email: String,
    age: Int
  )
  
  case class MaturePersonValidator(p: Person) extends Validator() {
    override val defaultError = ERR_AGE_OVER_18
    def isValid = p.age > 18
  }

  class PersonValidator(p: Person) extends FormValidator {
    val validators = Map(
	  "login" -> List(
	    RequiredStringValidator(p.login, Some(ERR_STR_REQ)),
	    MinLengthValidator(p.login, 2),
	    MaxLengthValidator(p.login, 6, Some(ERR_LOGIN_LONG))),
      "email" -> List(
        RequiredStringValidator(p.email, Some(ERR_STR_REQ)), 
        SimpleEmailValidator(p.email, Some(ERR_EMAIL_FORMAT))),
      "age" -> List(
        PositiveIntValidator(p.age, Some(ERR_AGE_POS_INT)), 
        MaturePersonValidator(p)))
  }
  
  val person = Person(login = "123456xx", email = null, age = 17)
  
  val personForm = new PersonValidator(person)
  
  val formErrors = Map(
    "login" -> ERR_LOGIN_LONG,
    "email" -> ERR_STR_REQ,
    "age" -> ERR_AGE_OVER_18
  )
  
  val loginAndAgeErrors = Map(
    "login" -> ERR_LOGIN_LONG,
    "age" -> ERR_AGE_OVER_18
  )
}