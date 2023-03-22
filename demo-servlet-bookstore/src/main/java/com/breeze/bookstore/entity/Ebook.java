package com.breeze.bookstore.entity;

import lombok.Data;

@Data
public class Ebook {
	private String bid;
	private String bname;
	private double price;
	private String author;
	private String image;
	private EbookCategory ebookCategory;
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public EbookCategory getEbookCategory() {
		return ebookCategory;
	}
	public void setEbookCategory(EbookCategory ebookCategory) {
		this.ebookCategory = ebookCategory;
	}

	@Override
	public String toString() {
		return "Ebook{" +
				"bid='" + bid + '\'' +
				", bname='" + bname + '\'' +
				", price=" + price +
				", author='" + author + '\'' +
				", image='" + image + '\'' +
				", ebookCategory=" + ebookCategory +
				'}';
	}
}
