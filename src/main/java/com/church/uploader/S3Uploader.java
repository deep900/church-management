/**
 * 
 */
package com.church.uploader;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.church.exception.S3UploadException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class S3Uploader implements UploaderService {

	private AmazonS3 amazonClient;

	@Value("accessKey")
	private String accessKey;

	@Value("secretKey")
	private String secretKey;

	@Value("bucketName")
	private String bucketName;

	private void init() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.amazonClient = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTHEAST_1)
				.build();
		log.info("Amazon client initialized successfully.");
	}

	/**
	 * Uploads a given file to S3 Bucket.
	 * 
	 * @param filePattern
	 * @param file
	 * @throws S3UploadException
	 */
	public synchronized void uploadFile(String filePattern, File file) throws S3UploadException {
		try {
			init();
			PutObjectResult result = amazonClient.putObject(bucketName, filePattern, file);
			log.info(result.getETag());
		} catch (Exception err) {
			log.error("Error while uploading the file", err);
			throw new S3UploadException(err.getMessage());
		} finally {
			if (null != amazonClient) {
				amazonClient = null;
			}
		}
	}
}
