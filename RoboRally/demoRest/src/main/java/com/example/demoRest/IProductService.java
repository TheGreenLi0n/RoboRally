package com.example.demoRest;

import java.util.List;
public interface IProductService
{
    List<Game> findAll();
    public Game getProductById(int id);
    boolean addProduct(Game p);
    public boolean updateProduct(int id, Game p);
    public boolean deleteProductById(int id);
}