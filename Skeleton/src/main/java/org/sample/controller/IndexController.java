package org.sample.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.sample.controller.exceptions.InvalidTeamException;
import org.sample.controller.exceptions.InvalidUserException;
import org.sample.controller.pojos.SignupForm;
import org.sample.controller.pojos.TeamForm;
import org.sample.controller.service.SampleService;
import org.sample.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexController {

    @Autowired
    SampleService sampleService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
    	ModelAndView model = new ModelAndView("index");
    	model.addObject("signupForm", new SignupForm()); 
    	model.addObject("teamList", sampleService.getTeamList());
        return model;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid SignupForm signupForm, BindingResult result, RedirectAttributes redirectAttributes) {
    	ModelAndView model;    	
    	if (!result.hasErrors()) {
            try {
            	sampleService.saveFrom(signupForm);
            	model = new ModelAndView("show");
            } catch (InvalidUserException e) {
            	model = new ModelAndView("index");
            	model.addObject("page_error", e.getMessage());
            }
        } else {
        	model = new ModelAndView("index");
        }   	
    	return model;
    }
    
    @RequestMapping(value = "/security-error", method = RequestMethod.GET)
    public String securityError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("page_error", "You do have have permission to do that!");
        return "redirect:/";
    }
    
    @RequestMapping(value="/newteam", method = RequestMethod.GET)
     public ModelAndView newTeam() {
       	ModelAndView model = new ModelAndView("new-team");
       	model.addObject("teamForm", new TeamForm());    	
        return model;
        }
  
    
    @RequestMapping(value = "/createTeam", method = RequestMethod.POST)
   	public ModelAndView create(@Valid TeamForm teamForm, BindingResult result, RedirectAttributes redirectAttributes) {
   			ModelAndView model;    	
  				if (!result.hasErrors()) {
    					try {
    						sampleService.saveFrom(teamForm);
    						model = new ModelAndView("show");
    			}		 catch (InvalidTeamException d) {
    				model = new ModelAndView("new-team");
    				model.addObject("page_error", d.getMessage());
        }
    } else {
    	model = new ModelAndView("new-team");
    }   	
	return model;
  }
    
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView showProfileByUserId(
      @RequestParam(value = "userId", required = true) Long userId,
      HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    	
    	ModelAndView model = new ModelAndView("profile");
       	model.addObject("user", sampleService.getUser(userId));    	
        return model;
    }
}

