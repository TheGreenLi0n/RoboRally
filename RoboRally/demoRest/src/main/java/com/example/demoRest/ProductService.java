package com.example.demoRest;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService
{
    //creating an object of ArrayList
    ArrayList<Game> games = new ArrayList<Game>();

    public ProductService() {
    //adding products to the List
//        products.add(new Product(100, "Mobile", 9000.00));
//        products.add(new Product(101, "Smart TV",  60000.00));
//        products.add(new Product(102, "Washing Machine", 9000.00));
//        products.add(new Product(103, "Laptop", 24000.00));
//        products.add(new Product(104, "Air Conditioner", 30000.00));
//        products.add(new Product(105, "Refrigerator ", 10000.00));
        games.add(new Game(101));

    }

    @Override
    public List<Game> findAll()
    {
    //returns a list of product
        return games;
    }

    @Override
    public boolean addProduct(Game p) {
        games.add(p);
        return true;
    }

    @Override
    public Game getProductById(int id) {
        for(Game p : games) {
            if(p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean updateProduct(int id, Game p) {
        for(Game currProd : games) {
            if(currProd.getId() == id) {
                currProd.setId(p.getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteProductById(int id) {
        ArrayList<Game> newProducts = new ArrayList<Game>();
        int oldSize = games.size();
        games.forEach((product -> {
            if(product.getId() != id)
                    newProducts.add(product);
        }));
        games = newProducts;
        return oldSize < games.size() ? true : false;
    }
}