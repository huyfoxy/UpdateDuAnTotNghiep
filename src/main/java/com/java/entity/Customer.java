package com.java.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.PrePersist;

import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer implements UserDetails, CredentialsContainer {

    @Id
    private String customerId;
    private String password;
    private String fullname;
    private String email;
    private String photo = "";
    private Boolean enabled;
    private String roleId; // roleId sẽ có giá trị mặc định là "2"

    private boolean isUsing2FA;

    private String secret;

    @PrePersist
    public void prePersist() {
        if (roleId == null) { // Nếu roleId chưa có giá trị, gán mặc định là "2"
            this.roleId = "2";
        }
    }

    @OneToMany(mappedBy = "customer")
    private Collection<Order> orders;

    @OneToMany(mappedBy = "customer")
    private List<Save> saves;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("1".equals(roleId)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void eraseCredentials() {
        password = "";
    }

}

//package com.java.entity;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//
//import org.jboss.aerogear.security.otp.api.Base32;

//import org.springframework.security.core.CredentialsContainer;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//@SuppressWarnings("serial")
//@Data
//@AllArgsConstructor
//@Entity
//@Table(name = "customers")
//public class Customer implements UserDetails, CredentialsContainer {
//
//	@Id
//	private String customerId;
//	private String password;
//	private String fullname;
//	private String email;
//	private String photo = "";
//	private Boolean enabled;
//	private String roleId;
//
//	private boolean isUsing2FA;
//
//	private String secret;
//
//	public Customer() {
//		super();
//		this.secret = Base32.random();
//		this.enabled = false;
//	}
//
//	@OneToMany(mappedBy = "customer")
//	private Collection<Order> orders;
//
////	@OneToMany(mappedBy = "customer")
////	private Collection<Role> roles;
//
//	@OneToMany(mappedBy = "customer")
//	private List<Save> saves;
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		List<GrantedAuthority> authorities = new ArrayList<>();
//		if ("1".equals(roleId)) {
//			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//		} else {
//			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//		}
//		return authorities;
//	}
//
//	@Override
//	public String getUsername() {
//		return email;
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return true;
//	}
//
//	@Override
//	public void eraseCredentials() {
//		password = "";
//	}
//
//	public String getCustomerId() {
//		return customerId;
//	}
//
//	public void setCustomerId(String customerId) {
//		this.customerId = customerId;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getFullname() {
//		return fullname;
//	}
//
//	public void setFullname(String fullname) {
//		this.fullname = fullname;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getPhoto() {
//		return photo;
//	}
//
//	public void setPhoto(String photo) {
//		this.photo = photo;
//	}
//
//	public Boolean getEnabled() {
//		return enabled;
//	}
//
//	public void setEnabled(Boolean enabled) {
//		this.enabled = enabled;
//	}
//
//	public String getRoleId() {
//		return roleId;
//	}
//
//	public void setRoleId(String roleId) {
//		this.roleId = roleId;
//	}
//
//	public boolean isUsing2FA() {
//		return isUsing2FA;
//	}
//
//	public void setUsing2FA(boolean isUsing2FA) {
//		this.isUsing2FA = isUsing2FA;
//	}
//
//	public String getSecret() {
//		return secret;
//	}
//
//	public void setSecret(String secret) {
//		this.secret = secret;
//	}
//
//	public Collection<Order> getOrders() {
//		return orders;
//	}
//
//	public void setOrders(Collection<Order> orders) {
//		this.orders = orders;
//	}
//
////	public Collection<Role> getRoles() {
////		return roles;
////	}
////
////	public void setRoles(Collection<Role> roles) {
////		this.roles = roles;
////	}
//
//	public List<Save> getSaves() {
//		return saves;
//	}
//
//	public void setSaves(List<Save> saves) {
//		this.saves = saves;
//	}
//	
//	
//}
