package com.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.model.Item;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
	
	private List<ItemDto> items;

}
