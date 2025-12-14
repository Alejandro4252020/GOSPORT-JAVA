package com.proyecto.gosports.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.proyecto.gosports.model.Usuario;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
        this.usuario = usuario;
        this.authorities = authorities;
    }

    // --- Métodos personalizados ---
    public Long getId() {
        return usuario.getId();
    }

    public String getRol() {
        return usuario.getRol();
    }

    public String getFotoPerfil() {
        return usuario.getFotoPerfil();
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    // --- Métodos obligatorios de Spring Security ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getUsername();
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
}
