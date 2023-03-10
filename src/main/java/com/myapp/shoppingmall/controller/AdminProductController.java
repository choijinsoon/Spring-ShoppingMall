package com.myapp.shoppingmall.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myapp.shoppingmall.dao.CategoryRepository;
import com.myapp.shoppingmall.dao.ProductRepository;
import com.myapp.shoppingmall.entities.Category;
import com.myapp.shoppingmall.entities.Product;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;
    
    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
        int perPage = 6;
        Pageable pageable = PageRequest.of(page, perPage);
        
        Page<Product> products = productRepo.findAll(pageable);
        List<Category> categories = categoryRepo.findAll();
        
        HashMap<Integer, String> cateIdAndName = new HashMap<>();
        for(Category category : categories)
            cateIdAndName.put(category.getId(), category.getName());
        
        model.addAttribute("products", products);
        model.addAttribute("cateIdAndName", cateIdAndName);

        long count = productRepo.count();
        double pageCount = Math.ceil((double)count / (double)perPage);
        
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "admin/products/index";
    }
    
    @GetMapping("/add")
    public String add(@ModelAttribute Product product, Model model){
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);

        return "admin/products/add";
    }

    @PostMapping("/add") 
    public String add(@Valid Product product, BindingResult bindingResult, @RequestParam(value = "file", required = false) MultipartFile file,
                                            RedirectAttributes attr, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryRepo.findAll();
            model.addAttribute("categories", categories);
            return "admin/products/add";	
        }
        
        boolean fileOk = false;
        byte[] bytes = file.getBytes();	
        String fileName = file.getOriginalFilename();	
        Path path = Paths.get("src/main/resources/static/media/" + fileName); 
        
        if (fileName.endsWith("jpg") || fileName.endsWith("png")) 
            fileOk = true;	
        

        attr.addFlashAttribute("message", "????????? ??????????????? ?????????!");
        attr.addFlashAttribute("alertClass", "alert-success");
        
        String slug = product.getName().toLowerCase().replace(" ", "-");
        
        Product productExists = productRepo.findByName(product.getName());
        
        if(!fileOk) {	
            attr.addFlashAttribute("message", "???????????? jpg??? png????????? ??????????????????!");
            attr.addFlashAttribute("alertClass", "alert-danger");
            attr.addFlashAttribute("product", product);
            
        } else if (productExists != null) {	// ????????? ???????????? DB??? ??????
            attr.addFlashAttribute("message", "?????? ???????????? ??????????????????.");
            attr.addFlashAttribute("alertClass", "alert-danger");
            attr.addFlashAttribute("product", product);
            
        } else {	
            product.setSlug(slug);
            product.setImage(fileName);
            productRepo.save(product);
            
            Files.write(path, bytes);	
        }
        return "redirect:/admin/products/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model){
        Product product = productRepo.getById(id);
        List<Category> categories = categoryRepo.findAll();

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);

        return "admin/products/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Product product, BindingResult bindingResult, MultipartFile file, RedirectAttributes attr, Model model) throws IOException{
        Product currentProduct = productRepo.getById(product.getId());

        if(bindingResult.hasErrors()){
            List<Category> categories = categoryRepo.findAll();
            model.addAttribute("categories", categories);
            if(product.getImage() == null)
                product.setImage(currentProduct.getImage());

            return "admin/products/edit";
        }

        boolean fileOk = false;
        byte[] bytes = file.getBytes();
        String fileName = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + fileName);

        if(!file.isEmpty()){
            if(fileName.endsWith("jpg") || fileName.endsWith("png"))
                fileOk = true;
        } else {
            fileOk = false;
        }

        attr.addFlashAttribute("message", "????????? ?????????");
        attr.addFlashAttribute("alertClass", "alert-success");

        String slug = product.getName().toLowerCase().replace(" ", "-");

        Product productExist = productRepo.findBySlugAndIdNot(slug, product.getId());

        if(!fileOk){
            attr.addFlashAttribute("message", "???????????? jpg??? png??? ????????? ?????????");
            attr.addFlashAttribute("alertClass", "alert-danger");
            attr.addFlashAttribute("product", product);
        } else if(productExist != null){
            attr.addFlashAttribute("message", "????????? ?????? ????????????. ???????????? ????????????");
            attr.addFlashAttribute("alertClass", "alert-danger");
            attr.addFlashAttribute("product", product);
        } else {
            product.setSlug(slug);

            if(!file.isEmpty()){
                Path currentPath = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
                Files.delete(currentPath);
                product.setImage(fileName);
                Files.write(path, bytes);
            } else{
                product.setImage(currentProduct.getImage());
            }

            productRepo.save(product);
        }
        return "redirect:/admin/products/edit/"+product.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes attr) throws IOException{
        Product currentProduct = productRepo.getById(id);
        Path currentPath = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());

        Files.delete(currentPath);
        productRepo.deleteById(id);

        attr.addFlashAttribute("message", "??????????????? ?????? ???????????????.");
        attr.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/products";
    }
}
