package com.sachin.controller;

import com.sachin.entity.*;
import com.sachin.service.FileMasterService;
import com.sachin.service.SkillService;
import com.sachin.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private FileMasterService fileMasterService;

    //File Handling
    @GetMapping("/getfile/{id}")
    public ResponseEntity files(@PathVariable("id") int id) throws Exception {
        FileMaster file_master = fileMasterService.getFileMasterById(id);
        if (file_master == null) {
            return ResponseEntity.badRequest().body("file not found");
        }
        byte[] image = Files.readAllBytes(Paths.get(file_master.getFileDirectory()));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }



    //Form
    @GetMapping("/")
    public String showForm(Model model) {
        List<Gender> genderList = new ArrayList<Gender>();
        genderList.add(new Gender(GenderType.male));
        genderList.add(new Gender(GenderType.female));
        model.addAttribute("gendersList", genderList);
        model.addAttribute("skillsList", Arrays.asList(new String[]{"Java", "Spring Boot", "Javascript", "MySql"}));
        model.addAttribute("userDetailsAndSkills", new UserDetailsAndSkills());
        return "user_details_form";
    }


    //Adding User After Form Fill
    @PostMapping("/addUserDetails")
    public ModelAndView addUserDetails(@ModelAttribute("userDetailsAndSkills") @Valid UserDetailsAndSkills userDetailsAndSkills
            , BindingResult errors
            , @RequestParam("oldEmail") String oldEmail
            , @RequestParam("image") MultipartFile multipartFile, ModelAndView modelAndView
    )
            throws IOException {


        if (errors.hasErrors()) {
            modelAndView.addObject("skillsList", Arrays.asList(new String[]{"Java", "Spring Boot", "Javascript", "MySql"}));
            modelAndView.addObject("userDetailsAndSkills",userDetailsAndSkills);
            modelAndView.setViewName("user_details_form");
            return modelAndView;
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        for (String skill : userDetailsAndSkills.getSkills()) {
            //Adding Skills
            if(!(skillService.needUpdate(oldEmail,userDetailsAndSkills,skill)))
                continue;
            Skill currSkill = skillService.addSkill(skill, userDetailsAndSkills.getEmail());
            skillService.addSkill(currSkill);
        }

        //File Handling
        Files.copy(multipartFile.getInputStream(), Paths.get("E:\\SpringBoot\\Project5\\src\\main\\resources\\static\\images\\" + fileName), StandardCopyOption.REPLACE_EXISTING);
        FileMaster fileMaster = fileMasterService.addFileMaster(fileName, userDetailsAndSkills.getEmail());
        fileMasterService.addFileMaster(fileMaster);

        //Adding photo url to userDetailsAndSkill
        userDetailsAndSkills.setPhotos("http://localhost:8080/getfile/" + String.valueOf(fileMaster.getId()));

        //Fetching userDetails and adding
        userDetailsService.addUserDetails(userDetailsAndSkills.getUserDetails());


        //Sending Object from controller to view
        modelAndView.addObject("userDetailsAndSkills", userDetailsAndSkills);
        modelAndView.setViewName("user_details_success");
        return modelAndView;
    }


    //Update Form
    @GetMapping("/updateUser/{email}")
    public  ModelAndView showUpdateForm(@PathVariable("email") String email,ModelAndView modelAndView)
    {
        List<Gender> genderList=new ArrayList<Gender>();
        genderList.add(new Gender(GenderType.male));
        genderList.add(new Gender(GenderType.female));
        modelAndView.addObject("userDetailsAndSkills"
                ,new UserDetailsAndSkills().getUserDetailsAndSkills(userDetailsService.getUserDetailsByEmail(email),
                        skillService.getAllNameByUserDetailsId(email)));
        modelAndView.addObject("gendersList",genderList);
        modelAndView.addObject("skillsList", Arrays.asList(new String[]{"Java","Spring Boot","Javascript","MySql"}));
        modelAndView.setViewName("update_user_details_form");
        return modelAndView;
    }

    //Update the details
    @PostMapping("/updateEditedUserDetails")
    public ModelAndView updateEditedUserDetails(@Valid @ModelAttribute UserDetailsAndSkills userDetailsAndSkills
            ,BindingResult errors
            , @RequestParam("oldEmail") String oldEmail
            , @RequestParam("updateimage") MultipartFile multipartFile, ModelAndView modelAndView)
            throws IOException
    {


        if (errors.hasErrors()) {
            List<Gender> genderList = new ArrayList<Gender>();
            genderList.add(new Gender(GenderType.male));
            genderList.add(new Gender(GenderType.female));
            modelAndView.addObject("gendersList", genderList);
            modelAndView.addObject("skillsList", Arrays.asList(new String[]{"Java", "Spring Boot", "Javascript", "MySql"}));
            modelAndView.addObject("userDetailsAndSkills",userDetailsAndSkills);
            modelAndView.setViewName("update_user_details_form");
            return modelAndView;
        }
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        //if past data not in present data like user a has [java] then after update a has [spring boot]
        //as java is already present in database
        //so we must remove the java and add spring boot to it

        skillService.deleteOlderSkillsWhichHeForgot(oldEmail,userDetailsAndSkills.getSkills());

        for(String skill:userDetailsAndSkills.getSkills())
        {

            //Before Updating check that a particular user email  already had that skill or not if yes dont update and also
            //check only if oldEmail=newEmail
            if(!(skillService.needUpdate(oldEmail,userDetailsAndSkills,skill)))
                continue;

            //Updating Skills
            Skill currSkill=skillService.addSkill(skill,userDetailsAndSkills.getEmail());
            skillService.updateSkill(oldEmail,currSkill);
        }

        //File Handling
        Files.copy(multipartFile.getInputStream(), Paths.get("E:\\SpringBoot\\Project5\\src\\main\\resources\\static\\images\\"+fileName), StandardCopyOption.REPLACE_EXISTING);

        FileMaster fileMaster=fileMasterService.addFileMaster(fileName,userDetailsAndSkills.getEmail());
        fileMasterService.updateFileMaster(oldEmail,fileMaster);

        //Adding photo url to userDetailsAndSkill
        userDetailsAndSkills.setPhotos("http://localhost:8080/getfile/"+String.valueOf(fileMaster.getId()));

        //Updating userDetails
        userDetailsService.updateUserDetails(oldEmail,userDetailsAndSkills.getUserDetails());


        //Sending Object from controller to view
        modelAndView.addObject("userDetailsAndSkills",userDetailsAndSkills);
        modelAndView.setViewName("user_details_success");
        return modelAndView;
    }

}
