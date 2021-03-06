package com.izera2.canny.rule;

import com.izera2.canny.interfaces.Translator;
import com.izera2.canny.interfaces.User;
import com.izera2.canny.utils.Dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RuleSet {

   List<Rule> rules = new ArrayList<Rule>();
   int actionType = ALLOW;
   //-----------------------------------------

   static int  ALLOW  = 1;
   static int DENY = 0;

   public RuleSet(List<Rule> rules) {
      this.rules = rules;
   }
   public RuleSet(Rule... rules) {
      this.rules = Arrays.asList(rules);
   }
   //-------------------------------------------------------------
   public RuleSet(List<Rule> rules, int actionType) {
      this.rules = rules;
      this.actionType = actionType;
   }
   //-------------------------------------------------------------

   public List<Rule> getRules() {
      return rules;
   }

   public void setRules(List<Rule> rules) {
      this.rules = rules;
   }

//------------------------------------------------

   public boolean can(User user, Object object) {
      for (Rule rule : rules) {
         if (!rule.can(user, object))
            return false;
      }
      return true;
   }
   
//   public boolean canAllow(User user, Object object) {
//      for (Rule rule : rules) {
//         if (!rule.can(user, object))
//            return false;
//      }
//      return true;
//   }
//   public boolean canDeny(User user, Object object) {
//      return !canAllow(user,object);
//   }

   public List<String> getErrors(User user, Object object, Translator translator) {
      List<String> errors = new ArrayList<String>();
      for (Rule rule : rules) {
         if (!rule.can(user, object))
            errors.add(rule.getErrorMessage(translator, Dynamic.getLocale(user), user, object));
      }
      return errors;
   }

   public String toString(){
      String ouput = "";
      for (Rule rule : rules) {
         ouput+= rule.getErrorMessage() +" AND ";
      }
      if(ouput.endsWith(" AND "))
         ouput = ouput.substring(0, ouput.length()-5);
      return ouput;
   }

   public String toString(User user, Object object) {
       String ouput = "";
      for (Rule rule : rules) {
         if(rule.can(user,object))
            ouput+= rule.getErrorMessage()+"* AND ";
         else
            ouput+= rule.getErrorMessage()+" AND ";
      }
      if(ouput.endsWith(" AND "))
         ouput = ouput.substring(0, ouput.length()-5);
      if(actionType == DENY)
         ouput+=" => DENY";
      else
         ouput+=" => FAILED";
      return ouput;
   }


}
