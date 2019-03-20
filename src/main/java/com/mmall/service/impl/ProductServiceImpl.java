package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
       if (product!=null){
           if (StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray=product.getSubImages().split(",");
                if (subImageArray.length>0){
                    product.setMainImage(subImageArray[0]);
                }
           }
           if (product.getId()!=null){
               int rowCount=productMapper.updateByPrimaryKey(product);
               if (rowCount>0)
               {
                   return  ServerResponse.createBySuccess("更新产品成功");}
               else {
                   return  ServerResponse.createByErrorMessage("更新产品失败");
               }
           }
           else {
               int rowCount= productMapper.insert(product);
               if (rowCount>0)
               {
                   return  ServerResponse.createBySuccess("新增产品成功");}
               else {
                   return  ServerResponse.createByErrorMessage("新增产品失败");
               }
           }

       }
       return ServerResponse.createByErrorMessage("新增或者更新产品失败");
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
      if (productId==null||status==null){
          return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
      }
      Product product=new Product();
      product.setId(productId);
      product.setStatus(status);
      int rowCount=productMapper.updateByPrimaryKeySelective(product);
      if (rowCount>0){
          return  ServerResponse.createBySuccess("修改产品销售状态成功");
      }
        return  ServerResponse.createBySuccess("修改产品销售状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {

            if (productId==null){
                return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode()
                        ,ResponseCode.ILLEGAL_ARGUMENT.getDesc());
            }
            Product product=productMapper.selectByPrimaryKey(productId);
            if (product==null){
                return ServerResponse.createByErrorMessage("产品以及下架或者删除");
            }
        ProductDetailVo productDetailVo=assembleProductDetailVo(product);
            return  ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        //startPage --start
        PageHelper.startPage(pageNum,pageSize);
        //填充自己sql查询逻辑
        List<ProductListVo> productListVoList=Lists.newArrayList();
        List<Product> productList=productMapper.selectList();//pagerhelper实际上返回的只是page对象
        for (Product productItem:productList){
            ProductListVo productListVo=assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        //pageHelper-收尾
        PageInfo pageResult=new PageInfo(productList);
        //第一步传入productList只是借来算页面信息的，
        // 实际展示给前端的还是展示productListVoList对象。
        pageResult.setList(productListVoList);

        return  ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<PageInfo> searchProdct(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)){
            productName=new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList=productMapper.selectByNameAndProductId(productName, productId);
        List<ProductListVo> productListVoList=Lists.newArrayList();
        for (Product productItem:productList){
            ProductListVo productListVo=assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult=new PageInfo(productList);
        pageResult.setList(productListVoList);
        return  ServerResponse.createBySuccess(pageResult);

    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo=new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setImageHost(PropertiesUtil.getProperty(
                PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/")));
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return  productListVo;
    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo=new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        //imageHost
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        //parentCategoryId
        Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category==null){
            //如果找不到默认根节点
            productDetailVo.setParentCategoryId(0);
        }else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        //createtime
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        //updatetime
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        //updatetime
        return  productDetailVo;
    }
}