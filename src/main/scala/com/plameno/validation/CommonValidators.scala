package com.plameno.validation

import scala.util.matching.Regex

/**
 * @author Alex Siman [https://github.com/siman]
 * @date 2013-09-27
 */

case class RequiredStringValidator(s: String, customError: Option[String] = None) 
	extends Validator(customError) {
  override val defaultError = "Field is required"
  def isValid = s != null && !s.trim.isEmpty
}

case class MinLengthValidator(s: String, minLen: Int, customError: Option[String] = None) 
	extends Validator(customError) {
  override val defaultError = s"Minimum length of a string is $minLen characters"
  def isValid = s.length >= minLen
}

case class MaxLengthValidator(s: String, maxLen: Int, customError: Option[String] = None) 
	extends Validator(customError) {
  override val defaultError = s"Maximum length of a string is $maxLen characters"
  def isValid = s.length <= maxLen
}

case class RegexValidator(s: String, re: Regex, customError: Option[String] = None)
  extends Validator(customError) {
  override val defaultError = s"Regular expression '${re}' could not be matched"
  def isValid = !re.findAllIn(s).isEmpty
}

case class SimpleEmailValidator(e: String, customError: Option[String] = None) 
	extends Validator(customError) {
  override val defaultError = "Invalid email"
  def isValid = RegexValidator(e,"""(\w+)@([\w\.]+)""".r, customError).isValid
}

case class PositiveIntValidator(i: Int, customError: Option[String] = None) 
	extends Validator(customError) {
  override val defaultError = "Must be a positive integer"
  def isValid = i > 0
}

case class InCaseValidator[A](s: A, f: A => Boolean, customError: Option[String] = None) 
  extends Validator(customError) {
  override val defaultError = "Predicate failed, test expression was fault"
  def isValid = f(s)
}