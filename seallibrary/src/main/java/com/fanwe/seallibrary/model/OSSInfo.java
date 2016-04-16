package com.fanwe.seallibrary.model;

import java.io.Serializable;

public class OSSInfo implements Serializable{
	private static final long serialVersionUID = -5785577506398029021L;
	
	public String host;
	public String accessId;
	public String accessKey;
	public String bucket;
	public String url;
	public String callback;
}
