package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Reputation;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReputationEditForm;
import com.example.samuraitravel.form.ReputationForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReputationRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReputationService;

@RequestMapping("/houses/{houseId}/reviews")
@Controller
public class ReputationController {
	private final HouseRepository houseRepository;     
	private final ReputationRepository reputationRepository;

	private final ReputationService reputationService;
	
    
    public ReputationController(HouseRepository houseRepository,ReputationRepository reputationRepository, ReputationService reputationService) {
        this.houseRepository = houseRepository;      
        this.reputationRepository = reputationRepository;
        this.reputationService = reputationService;
        

}
    @GetMapping
    public String index(@PathVariable(name = "houseId") Integer houseId, Model model) {
    	House house = houseRepository.getReferenceById(houseId);
    	
    	
    	model.addAttribute("reputationForm", new ReputationForm());   
    	model.addAttribute("house", house);   
    	 
    	
    	return "reviews/index";
}
    @GetMapping("/list")
    public String list(@PathVariable(name = "houseId") Integer houseId,Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {
    	 House house = houseRepository.getReferenceById(houseId);
    	Page<Reputation> reputationPage = reputationRepository.findByHouseOrderByCreatedAtDesc(house, pageable);
    	  	 
    	 model.addAttribute("reputationPage", reputationPage);
    	 model.addAttribute("house", house);   
    	 
    	 return "reviews/show";   	
    }
    @PostMapping("/create")
    public String create(@PathVariable(name = "houseId") Integer houseId,@ModelAttribute @Validated ReputationForm reputationForm,
    		BindingResult bindingResult,
    		@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,  
    		 RedirectAttributes redirectAttributes, 
    		Model model) {
    	  
    	  User user = userDetailsImpl.getUser(); 
    	  House house = houseRepository.getReferenceById(houseId);
    	  
    	  if (bindingResult.hasErrors()) {
    		  model.addAttribute("house", house);   
              return "reviews/index";
          }
    	  reputationService.create(reputationForm, user, house);
    	 
    	  
          redirectAttributes.addFlashAttribute("successMessage", "民宿を登録しました。"); 
        
          
          
          return "redirect:/houses/{houseId}";
    }
    @PostMapping("/{reputationPageId}/delete")
    public String delete(@PathVariable(name = "houseId") Integer houseId, @PathVariable("reputationPageId")  Integer reputationPageId, RedirectAttributes redirectAttributes) {        
        reputationRepository.deleteById(reputationPageId);
                
        redirectAttributes.addFlashAttribute("successMessage", "投稿を削除しました。");
        
        return "redirect:/houses/" + houseId;
    }
    @GetMapping("/{reputationPageId}/edit")
    public String edit(@PathVariable(name = "houseId") Integer houseId, @PathVariable("reputationPageId")  Integer reputationPageId, Model model) {
        Reputation reputationPage = reputationRepository.getReferenceById(reputationPageId);
        House house = houseRepository.getReferenceById(houseId);
        ReputationEditForm reputationEditForm = new ReputationEditForm(reputationPage.getScore(), reputationPage.getDescription());
        
        
        model.addAttribute("reputationEditForm", reputationEditForm);
        model.addAttribute("house", house);  
        model.addAttribute("reputationPage", reputationPage);  
        
        return "reviews/edit";
    }    
    @PostMapping("/{reputationPageId}/update")
    public String update(@ModelAttribute @Validated ReputationEditForm reputationEditForm, 
    		@PathVariable(name = "houseId") Integer houseId, @PathVariable("reputationPageId")  Integer reputationPageId, BindingResult bindingResult, 
    		RedirectAttributes redirectAttributes,@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {        
        if (bindingResult.hasErrors()) {
            return "reviews/edit";
        }
        User user = userDetailsImpl.getUser(); 
  	    House house = houseRepository.getReferenceById(houseId);
        
        reputationService.update(reputationEditForm, user, house, reputationPageId);
        redirectAttributes.addFlashAttribute("successMessage", "民宿情報を編集しました。");
        
        return "redirect:/houses/" + houseId;
    }    
}
