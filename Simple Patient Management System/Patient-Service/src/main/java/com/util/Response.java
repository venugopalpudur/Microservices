package com.util;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatusCode;

import com.model.Services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Response {

	private String port;
	private Services service;
	private List<Services> services;
}
