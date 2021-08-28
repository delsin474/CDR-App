package com.rakuten.springboot.cdr.controller.v1.ui;

import com.rakuten.springboot.cdr.controller.v1.command.*;
import com.rakuten.springboot.cdr.dto.model.user.UserDto;
import com.rakuten.springboot.cdr.dto.model.cdr.*;
import com.rakuten.springboot.cdr.service.CdrDetailService;
import com.rakuten.springboot.cdr.service.FileParserService;
import com.rakuten.springboot.cdr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Mohit Chandak
 */
@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileParserService fileParserService;

    @Autowired
    private CdrDetailService cdrDetailService;

    @GetMapping(value = "/dashboard")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("dashboard");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentUser", userDto);
        modelAndView.addObject("userName", userDto.getFullName());
        return modelAndView;
    }

    @GetMapping(value = "/files")
    public ModelAndView fileDetails() {
        ModelAndView modelAndView = new ModelAndView("files");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //List of Files
        List<FileDto> allFiles = fileParserService.getAllFiles();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        FileFormCommand fileFormCommand = new FileFormCommand();
        modelAndView.addObject("files", allFiles);
        modelAndView.addObject("fileFormData", fileFormCommand);
        modelAndView.addObject("userName", userDto.getFullName());
        return modelAndView;
    }

    @PostMapping(value = "/files")
    public ModelAndView parseFile(@Valid @ModelAttribute("fileFormData") FileFormCommand fileFormCommand) {
        ModelAndView modelAndView = new ModelAndView("files");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", userDto.getFullName());
        fileParserService.parseFile(fileFormCommand.getFileName());
        return modelAndView;
    }

    @GetMapping(value = "/cdroperations")
    public ModelAndView cdrOperations() {
        ModelAndView modelAndView = new ModelAndView("cdroperations");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        DateFormCommand dateFormCommand = new DateFormCommand();
        //Form
        modelAndView.addObject("dateFormData", dateFormCommand);
        modelAndView.addObject("userName", userDto.getFullName());
        return modelAndView;
    }
    
    @GetMapping(value = "/getanumhighestduration")
    public ModelAndView getAnumHighestDuration() {
        ModelAndView modelAndView = new ModelAndView("cdroperations");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        String anum = cdrDetailService.getAnumHighestDuration();
        //Debug
        //System.out.println("ANUM : " + anum);
        modelAndView.addObject("highestDurationAnum",anum);
        //Form
        DateFormCommand dateFormCommand = new DateFormCommand();
        modelAndView.addObject("dateFormData", dateFormCommand);
        modelAndView.addObject("userName", userDto.getFullName());
        return modelAndView;
    }

    @GetMapping(value = "/getanumhighestchargebyday")
    public ModelAndView getAnumHighestChargeByDay() {
        ModelAndView modelAndView = new ModelAndView("cdroperations");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        List<AnumChargeDto> anumsCharge = cdrDetailService.getAnumByMaxChargePerDay();
        modelAndView.addObject("anumHighestCharge",anumsCharge);
        //Form
        DateFormCommand dateFormCommand = new DateFormCommand();
        modelAndView.addObject("dateFormData", dateFormCommand);
        modelAndView.addObject("userName", userDto.getFullName());
        return modelAndView;
    }

    @GetMapping(value = "/getservicehighestchargebyday")
    public ModelAndView getServiceHighestChargeByDay() {
        ModelAndView modelAndView = new ModelAndView("cdroperations");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        List<ServiceChargeDto> serviceCharges = cdrDetailService.getServiceByMaxChargePerDay();
        modelAndView.addObject("serviceCharges",serviceCharges);
        //Form
        DateFormCommand dateFormCommand = new DateFormCommand();
        modelAndView.addObject("dateFormData", dateFormCommand);
        modelAndView.addObject("userName", userDto.getFullName());
        return modelAndView;
    }

    @GetMapping(value = "/generatecdroutjson")
    public ModelAndView getCdrOutJson() {
        ModelAndView modelAndView = new ModelAndView("cdroperations");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        boolean success = cdrDetailService.generateOutputJson();
        modelAndView.addObject("isSuccess",success);
        //Form
        DateFormCommand dateFormCommand = new DateFormCommand();
        modelAndView.addObject("dateFormData", dateFormCommand);
        modelAndView.addObject("userName", userDto.getFullName());
        return modelAndView;
    }

    @PostMapping(value = "/getvolumebydate")
    public ModelAndView getVolumeByDate(@Valid @ModelAttribute("dateFormData") DateFormCommand dateFormCommand) {
        ModelAndView modelAndView = new ModelAndView("cdroperations");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        Long volume = cdrDetailService.getVolumePerDayMinutes(dateFormCommand.getDate());
        modelAndView.addObject("volumeOnDate",volume);
        modelAndView.addObject("dateFormData", dateFormCommand);
        modelAndView.addObject("userName", userDto.getFullName());
        return modelAndView;
    }

    @GetMapping(value = "/profile")
    public ModelAndView getUserProfile() {
        ModelAndView modelAndView = new ModelAndView("profile");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        ProfileFormCommand profileFormCommand = new ProfileFormCommand()
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setMobileNumber(userDto.getMobileNumber());
        PasswordFormCommand passwordFormCommand = new PasswordFormCommand()
                .setEmail(userDto.getEmail())
                .setPassword(userDto.getPassword());
        modelAndView.addObject("profileForm", profileFormCommand);
        modelAndView.addObject("passwordForm", passwordFormCommand);
        modelAndView.addObject("userName", userDto.getFullName());
        return modelAndView;
    }

    @PostMapping(value = "/profile")
    public ModelAndView updateProfile(@Valid @ModelAttribute("profileForm") ProfileFormCommand profileFormCommand, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("profile");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        PasswordFormCommand passwordFormCommand = new PasswordFormCommand()
                .setEmail(userDto.getEmail())
                .setPassword(userDto.getPassword());
        modelAndView.addObject("passwordForm", passwordFormCommand);
        modelAndView.addObject("userName", userDto.getFullName());
        if (!bindingResult.hasErrors()) {
            userDto.setFirstName(profileFormCommand.getFirstName())
                    .setLastName(profileFormCommand.getLastName())
                    .setMobileNumber(profileFormCommand.getMobileNumber());
            userService.updateProfile(userDto);
            modelAndView.addObject("userName", userDto.getFullName());
        }
        return modelAndView;
    }

    @PostMapping(value = "/password")
    public ModelAndView changePassword(@Valid @ModelAttribute("passwordForm") PasswordFormCommand passwordFormCommand, BindingResult bindingResult) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("profile");
            ProfileFormCommand profileFormCommand = new ProfileFormCommand()
                    .setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName())
                    .setMobileNumber(userDto.getMobileNumber());
            modelAndView.addObject("profileForm", profileFormCommand);
            modelAndView.addObject("userName", userDto.getFullName());
            return modelAndView;
        } else {
            userService.changePassword(userDto, passwordFormCommand.getPassword());
            return new ModelAndView("login");
        }
    }

}
