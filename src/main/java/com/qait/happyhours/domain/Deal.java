package com.qait.happyhours.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "deal")
public class Deal implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "deal_id")
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "location", nullable = false)
	private String location;

	@Column(name = "latitude", nullable = false)
	private Double latitude;

	@Column(name = "longitude", nullable = false)
	private Double longitude;

	@Column(name = "originalPrice", nullable = false)
	private String originalPrice;

	@Column(name = "newPrice", nullable = false)
	private String newPrice;

	@Column(name = "discount", nullable = false)
	private String discount;

	@Column(name = "mainImage", unique = false, nullable = false)
	private String dealMainImage;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "deal_id")
	private Set<DealOffers> dealOffersList;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "deal_id")
	private Set<DealImages> dealImagesList;

	@Column(name = "start_date", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "end_date", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Column(name = "deal_type", unique = false, nullable = false)
	private Boolean dealType;

	@Column(name = "isExpired", unique = false, nullable = false)
	private Boolean isExpired;

	@Transient
	private Double relativeDistance;

	@Transient
	private Double requiredDistance;

	public Double getRequiredDistance() {
		return requiredDistance;
	}

	public void setRequiredDistance(Double requiredDistance) {
		this.requiredDistance = requiredDistance;
	}

	public Double getRelativeDistance() {
		return relativeDistance;
	}

	public void setRelativeDistance(Double relativeDistance) {
		this.relativeDistance = relativeDistance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Set<DealOffers> getDealOffersList() {
		return dealOffersList;
	}

	public void setDealOffersList(Set<DealOffers> dealOffersList) {
		this.dealOffersList = dealOffersList;
	}

	public Set<DealImages> getDealImagesList() {
		return dealImagesList;
	}

	public void setDealImagesList(Set<DealImages> dealImagesList) {
		this.dealImagesList = dealImagesList;
	}

	public String getDealMainImage() {
		return dealMainImage;
	}

	public void setDealMainImage(String dealMainImage) {
		this.dealMainImage = dealMainImage;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getDealType() {
		return dealType;
	}

	public void setDealType(Boolean dealType) {
		this.dealType = dealType;
	}

	public Boolean getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(Boolean isExpired) {
		this.isExpired = isExpired;
	}

}