package com.example.demoRest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController
{
    @Autowired
    private IProductService productService;

    @GetMapping(value = "/games")
    public ResponseEntity<List<Game>> getProduct()
    {
        List<Game> products = productService.findAll();
        return ResponseEntity.ok().body(products);
    }

    @PostMapping("/games")
    public ResponseEntity<String > addProduct(@RequestBody Game p) {
        boolean added = productService.addProduct(p);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

    @GetMapping("/games/{id}")
    public ResponseEntity<Game> getProductById(@PathVariable int id) {
        Game p = productService.getProductById(id);
        return ResponseEntity.ok().body(p);
    }

    @PutMapping("/games/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestBody Game p) {
        boolean added = productService.updateProduct(id, p);
        return ResponseEntity.ok().body("updated");
    }

    @DeleteMapping("/games/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        boolean deleted = productService.deleteProductById(id);
        return ResponseEntity.ok().body("deleted");
    }

}