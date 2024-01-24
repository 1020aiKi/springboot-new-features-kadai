package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

 @Data
public class ReputationForm { 
	 	 
	private Integer score;
	
	@NotBlank(message="文字を入力して下さい")
	private String description;
}
