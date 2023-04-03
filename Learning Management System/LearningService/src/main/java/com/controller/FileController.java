package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dto.CourseDTO;
import com.entity.Course;
import com.entity.FileResource;
import com.repository.ResourceRepository;
import com.service.CreationService;
import com.service.impls.FileStorageService;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@ExposesResourceFor(FileResource.class)
@CrossOrigin("http://localhost:4200/")
public class FileController{
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private ResourceRepository resourceRepository;
    
    /*@Autowired
    private ContentRepository contentRepository;*/
    
    @Autowired
    private CreationService creationService;

	
	/*@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
	@PostMapping("/courseUpload")
	public ResponseEntity<?> addCourses(@RequestBody CourseDTO courseDTO, @RequestParam("file") MultipartFile file, Authentication authentication) {
		
		FileController fileController = new FileController();
		//courseDTO.setFileResource(fileController.uploadFile(file));
		System.out.println(courseDTO);
		//Course addCourse = creationService.addCourse(courseDTO, authentication);
				
		return new ResponseEntity<>(courseDTO, HttpStatus.OK);
	}*/
   
    @PostMapping("upload")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
    private FileResource uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileName = file.getOriginalFilename();
            fileStorageService.storeFile(file, fileName);
            String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/").path(uuid).toUriString();
            
            FileResource resource = resourceRepository.save(new FileResource(
                    fileName, uuid, downloadUri, file.getContentType(), file.getSize()));
            //Content content = contentRepository.save(new Content("sdgsdg", "sgsdags"));
            return resource;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')")
    @PostMapping("uploads")
    @ResponseBody
    public List<FileResource> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }


    @GetMapping("download/{uuid:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String uuid, HttpServletRequest request) throws Exception {
        // Load file as resource
    	FileResource fileResource = resourceRepository.findByResourceId(uuid);
        Resource resource = fileStorageService.loadFileAsResource(fileResource.getName());
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
        	e.printStackTrace();
        }
        // Fall back to default type if type cannot be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        String header = String.format("attachment; file=%s", resource.getFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, header)
                .body(resource);
    }
}