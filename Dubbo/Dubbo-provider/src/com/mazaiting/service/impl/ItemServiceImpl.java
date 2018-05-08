package com.mazaiting.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.mazaiting.service.ItemService;

public class ItemServiceImpl implements ItemService{

	@Override
	public List<String> getList(Long id) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			list.add("item" + i);
		}
		return list;
	}

}
