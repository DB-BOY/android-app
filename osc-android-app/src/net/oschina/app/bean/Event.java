package net.oschina.app.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 活动实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年12月12日 下午3:18:08
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("event")
public class Event implements Serializable {
	
	@XStreamAlias("id")
	private int id;
	
	@XStreamAlias("img")
	private String img;
	
	@XStreamAlias("title")
	private String title;
	
	@XStreamAlias("url")
	private String url;
	
	@XStreamAlias("createTime")
	private String createTime;
	
	@XStreamAlias("startTime")
	private String startTime;
	
	@XStreamAlias("endTime")
	private String endTime;
	
	@XStreamAlias("spot")
	private String spot;
	
	@XStreamAlias("actor_count")
	private int actor_count;
	
	@XStreamAlias("location")
	private String location;
	
	@XStreamAlias("city")
	private String city;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSpot() {
		return spot;
	}

	public void setSpot(String spot) {
		this.spot = spot;
	}

	public int getActor_count() {
		return actor_count;
	}

	public void setActor_count(int actor_count) {
		this.actor_count = actor_count;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}