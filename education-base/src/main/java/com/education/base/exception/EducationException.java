package com.education.base.exception;

public class EducationException extends RuntimeException {

   private String errMessage;

   public EducationException() {
      super();
   }

   public EducationException(String errMessage) {
      super(errMessage);
      this.errMessage = errMessage;
   }

   public String getErrMessage() {
      return errMessage;
   }

   public static void cast(CommonError commonError){
       throw new EducationException(commonError.getErrMessage());
   }
   public static void cast(String errMessage){
       throw new EducationException(errMessage);
   }

}
