package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
@Service("iFileService")
public class FileServiceImpl implements IFileService{
    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        String fileName=file.getOriginalFilename();
        //扩展名
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1); //获取后缀名
        String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;  //随机改个名字防止重复
        logger.info("开始上传文件，上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);
        File fileDir=new File(path); //生成一个apache服务器暂时存储图片的路径
        if (!fileDir.exists()){         //若不存在，，，就生成一个咯
            fileDir.setWritable(true);//给予权限
            fileDir.mkdirs();
        }
        File targetFile=new File(path,uploadFileName);    //在指定路径下生成一个 文件，文件名为上面取得随机名
        try {
            file.transferTo(targetFile);   //将上传得文件赛进去目标文件仲。
            //文件已经上传成功
            //todo 将targetFile上传到我们的ftp服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));//已经上传到ftp服务器上

            //todo 上传完后删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }
//    public static void main(String[] args){
//        String fileNanme="abc.jpg";
//        System.out.println(fileNanme.substring(fileNanme.lastIndexOf(".")+1));
//    }

}
