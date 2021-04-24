/*
 * Copyright (c) 2021 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.facade;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User extends JsonSerializable {

	@Expose
	private int id;
	@Expose
	private String name;
	@Expose
	@SerializedName("created_at")
	private Date createdAt;
	private Date updatedAt;
	private Date nameChangedAt;
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getNameChangedAt() {
		return nameChangedAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

}
