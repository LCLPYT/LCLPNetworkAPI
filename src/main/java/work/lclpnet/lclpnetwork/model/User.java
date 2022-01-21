/*
 * Copyright (c) 2022 LCLP.
 *
 * Licensed under the MIT License. For more information, consider the LICENSE file in the project's root directory.
 */

package work.lclpnet.lclpnetwork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import work.lclpnet.lclpnetwork.util.JsonDateAdapter;

import java.util.Date;

/**
 * Represents a LCLPNetwork user.
 */
public class User extends JsonSerializable {

	@Expose
	private long id;
	@Expose
	private String name;
	@Expose(serialize = false)
	private String email;
	@Expose(serialize = false)
	@SerializedName("email_verified_at")
	private Date emailVerifiedAt;
	@Expose
	@SerializedName("created_at")
	private Date createdAt;
	@Expose
	@SerializedName("updated_at")
	private Date updatedAt;
	@Expose
	@SerializedName("name_changed_at")
	@JsonAdapter(JsonDateAdapter.YMDHMS.class)
	private Date nameChangedAt;

	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public Date getEmailVerifiedAt() {
		return emailVerifiedAt;
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
