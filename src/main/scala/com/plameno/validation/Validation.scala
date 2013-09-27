package com.plameno.validation

/**
 * @author Alex Siman [https://github.com/siman]
 * @date 2013-09-27
 */

abstract class Validator(customError: Option[String] = None) {
  val defaultError: String = "Field is invalid"
  def error: String = customError.getOrElse(defaultError)
  def validate: Option[String] = if (isValid) None else Some(error)
  def isValid: Boolean
  def isInvalid: Boolean = !isValid
}

abstract class FormValidator {
  
  /** List of all validators. */
  def validators: Map[String, List[Validator]]
  
  /**
   * Validate all fields in form.
   * Returns a map where:
   *   key - field name;
   *   value - validation error;
   */
  def validate: Map[String, String] = {
    validators.flatMap { case (f, vs) => 
      // Fail on first validation error per field
      findFirstError(vs).map(f -> _)
    }
  }
  
  /** Validate a list of fields. */
  def validateFields(fieldNames: List[String]): Map[String, String] = {
    if (fieldNames.isEmpty) Map.empty
    else fieldNames.flatMap(f => validateField(f).map(f -> _)).toMap
  }
  
  /** Validate single field. */
  def validateField(fieldName: String): Option[String] = {
    validators.get(fieldName).flatMap(findFirstError)
  }
  
  protected def findFirstError(vs: List[Validator]): Option[String] = {
    vs.collectFirst { case v if v.isInvalid => v.error }    
  }
}
