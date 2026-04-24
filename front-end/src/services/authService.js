import { apiRequest } from './api'

export function loginCliente(payload) {
  return apiRequest('/auth/clientes/login', {
	method: 'POST',
	body: payload,
  })
}

export function loginPrestador(payload) {
  return apiRequest('/auth/prestadores/login', {
	method: 'POST',
	body: payload,
  })
}

export function registerCliente(payload) {
  return apiRequest('/auth/clientes/registrar', {
	method: 'POST',
	body: payload,
  })
}

export function registerPrestador(payload) {
  return apiRequest('/auth/prestadores/registrar', {
	method: 'POST',
	body: payload,
  })
}

export function updateClienteProfile(payload) {
  return apiRequest('/clientes/me', {
	method: 'PUT',
	body: payload,
  })
}

export function updatePrestadorProfile(payload) {
  return apiRequest('/prestadores/me', {
	method: 'PUT',
	body: payload,
  })
}

export function updateClientePassword(payload) {
  return apiRequest('/clientes/me/senha', {
	method: 'PATCH',
	body: payload,
  })
}

export function updatePrestadorPassword(payload) {
  return apiRequest('/prestadores/me/senha', {
	method: 'PATCH',
	body: payload,
  })
}

export function getClienteProfile() {
  return apiRequest('/clientes/me', {
    method: 'GET',
  })
}

export function getPrestadorProfile() {
  return apiRequest('/prestadores/me', {
    method: 'GET',
  })
}
