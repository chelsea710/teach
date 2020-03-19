package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTest {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Test
    public void saveFile() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(new File("D:\\index_banner.ftl"));
        ObjectId index_banner = gridFsTemplate.store(fileInputStream, "index_banner");
        System.out.println(index_banner);
    }

    @Test
    public void getFileByGridFs() throws IOException {
        GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5e738ce0a7f7b7528cf5ea18")));
        GridFSDownloadStream stream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(fsFile,stream);

        System.out.println(IOUtils.toString(gridFsResource.getInputStream(),"utf-8"));
    }
}
