const API_BASE_URL = import.meta.env.VITE_API_URL || '/api'
const AUTH_STORAGE_KEY = 'cuidarplus.auth'

function buildUrl(path) {
  const normalizedBase = API_BASE_URL.endsWith('/') ? API_BASE_URL.slice(0, -1) : API_BASE_URL
  const normalizedPath = path.startsWith('/') ? path : `/${path}`
  return `${normalizedBase}${normalizedPath}`
}

function readAuthStorage() {
  try {
	const raw = localStorage.getItem(AUTH_STORAGE_KEY)
	return raw ? JSON.parse(raw) : null
  } catch {
	return null
  }
}

export function getStoredAuth() {
  return readAuthStorage()
}

export function setStoredAuth(session) {
  localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(session))
}

export function clearStoredAuth() {
  localStorage.removeItem(AUTH_STORAGE_KEY)
}

function getTokenFromStorage() {
  return readAuthStorage()?.token ?? null
}

async function readResponse(response) {
  if (response.status === 204) {
	return null
  }

  const text = await response.text()
  if (!text) {
	return null
  }

  try {
	return JSON.parse(text)
  } catch {
	return text
  }
}

function getErrorMessage(payload, fallback) {
  if (!payload) {
	return fallback
  }

  if (typeof payload === 'string') {
	return payload
  }

  return payload.message || payload.error || fallback
}

export async function apiRequest(path, options = {}) {
  const token = options.token ?? getTokenFromStorage()
  const headers = new Headers(options.headers || {})

  if (options.body !== undefined && !(options.body instanceof FormData)) {
	headers.set('Content-Type', 'application/json')
  }

  if (token) {
	headers.set('Authorization', `Bearer ${token}`)
  }

  const response = await fetch(buildUrl(path), {
	...options,
	headers,
	body:
	  options.body !== undefined && !(options.body instanceof FormData)
		? JSON.stringify(options.body)
		: options.body,
  })

  const payload = await readResponse(response)

  if (!response.ok) {
	throw new Error(
	  getErrorMessage(
		payload,
		'Não foi possível processar sua solicitação. Tente novamente.',
	  ),
	)
  }

  return payload
}

export const authStorage = {
  key: AUTH_STORAGE_KEY,
  get: readAuthStorage,
  set: setStoredAuth,
  clear: clearStoredAuth,
}

