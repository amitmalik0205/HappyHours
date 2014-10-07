package com.qait.happyhours.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dealImages")
public class DealImages implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	/*
	 * @Lob
	 * 
	 * @Column(name = "image", unique = false, nullable = false, length =
	 * 100000) private byte[] image;
	 */

	@Column(name = "image", unique = false, nullable = false)
	private String image;

	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deal_id")
	private Deal deal;*/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*public Deal getDeal() {
		return deal;
	}

	public void setDeal(Deal deal) {
		this.deal = deal;
	}
*/
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}