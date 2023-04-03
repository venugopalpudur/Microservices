package com.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.exceptions.NoDataFoundException;
import com.model.Image;
import com.model.Product;
import com.repository.ImageRepository;
import com.repository.ProductRepository;
import com.service.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ImageRepository imageRepository;

	@PostMapping("/products")
	public ResponseEntity<?> addProduct(@RequestBody Product product) throws NoDataFoundException {
		Product pro = productService.addProduct(product);
		if (pro != null) {
			return new ResponseEntity<>(pro, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. customer cannot be created");
		}
	}

	@PutMapping("/products/{productId}")
	public ResponseEntity<?> updateProduct(@PathVariable("productId") int productId, @RequestBody Product product)
			throws NoDataFoundException {
		Product pro = productService.updateProduct(productId, product);
		if (pro != null) {
			return new ResponseEntity<>(pro, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Customer cannot be updated");
		}
	}

	@GetMapping("/products/{productId}")
	public ResponseEntity<?> searchProduct(@PathVariable("productId") int productId) throws NoDataFoundException {
		Product pro = productService.searchProduct(productId);
		if (pro != null) {
			return new ResponseEntity<>(pro, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. customer details cannot be found");
		}
	}

	@DeleteMapping("/products/{productId}")
	public ResponseEntity<?> deleteteProduct(@PathVariable("productId") int productId) {

		return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
	}

	
	@GetMapping("/products/all")
	public ResponseEntity<?> allProducts() throws NoDataFoundException {
		List<Product> pro = productRepository.findAll();
		if (pro != null) {
			return new ResponseEntity<>(pro, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Product details cannot be found");
		}
	}
	
	@PostMapping("/upload/{id}")
	public ResponseEntity<?> uplaodImage(@PathVariable("id") int id, @RequestParam("image") MultipartFile file) throws IOException {

		System.out.println("Original Image Byte Size - " + file.getBytes().length);
		Image img = new Image();
		img.setProductId(id);
		img.setFile(compressBytes(file.getBytes()));	
		Image respImg = imageRepository.save(img);
		return new ResponseEntity<>(respImg, HttpStatus.OK);
	}

	@GetMapping(path = { "/get/{id}" })
	public ResponseEntity<?> getImage(@PathVariable("id") int id) throws IOException {

		 Image retrievedImage = imageRepository.findByProductId(id);
		Image img = new Image();
		img.setId(retrievedImage.getId());
		img.setProductId(retrievedImage.getProductId());
		img.setFile(decompressBytes(retrievedImage.getFile()));
		return new ResponseEntity<>(img, HttpStatus.OK);
	}

	// compress the image bytes before storing it in the database
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}
		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

		return outputStream.toByteArray();
	}

	// uncompress the image bytes before returning it to the angular application
	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
	
	@GetMapping("/getImages/all")
	public ResponseEntity<?> allimages() throws NoDataFoundException {
		List<Image> img = imageRepository.findAll();
		if (img != null) {
			return new ResponseEntity<>(img, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Product details cannot be found");
		}
	}
}
