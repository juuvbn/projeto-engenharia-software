/* eslint-disable react-refresh/only-export-components */
import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import { authStorage } from '../services/api'

const AuthContext = createContext(null)

function isSessionValid(session) {
  if (!session?.token) {
	return false
  }

  if (!session.expiresAt) {
	return true
  }

  return Date.now() < session.expiresAt
}

function normalizeSession(payload) {
  const session = {
	token: payload.token,
	tokenType: payload.tokenType || 'Bearer',
	expiresAt: payload.expiresIn,
	user: {
	  id: payload.id,
	  nome: payload.nome,
	  email: payload.email,
	  tipoUsuario: payload.tipoUsuario,
	},
  }

  return session
}

export function AuthProvider({ children }) {
  const [session, setSession] = useState(() => {
	const stored = authStorage.get()
	return isSessionValid(stored) ? stored : null
  })

  useEffect(() => {
	if (session) {
	  authStorage.set(session)
	} else {
	  authStorage.clear()
	}
  }, [session])

  useEffect(() => {
	if (!session?.expiresAt) {
	  return undefined
	}

	const delay = session.expiresAt - Date.now()
	const timeoutId = window.setTimeout(() => {
	  setSession(null)
	}, Math.max(delay, 0))

	return () => window.clearTimeout(timeoutId)
  }, [session?.expiresAt])

  const login = useCallback((payload) => {
	setSession(normalizeSession(payload))
  }, [])

  const logout = useCallback(() => {
	setSession(null)
  }, [])

  const updateUser = useCallback((data) => {
	setSession((current) => {
	  if (!current) {
		return current
	  }

	  const nextUser = {
		...current.user,
		...data,
	  }

	  const unchanged =
		current.user.nome === nextUser.nome &&
		current.user.email === nextUser.email &&
		current.user.tipoUsuario === nextUser.tipoUsuario &&
		current.user.id === nextUser.id

	  if (unchanged) {
		return current
	  }

	  return {
		...current,
		user: nextUser,
	  }
	})
  }, [])

  const value = useMemo(() => {
	const user = session?.user ?? null

	return {
	  token: session?.token ?? null,
	  tokenType: session?.tokenType ?? 'Bearer',
	  user,
	  isAuthenticated: Boolean(user),
	  isCliente: user?.tipoUsuario === 'CLIENTE',
	  isPrestador: user?.tipoUsuario === 'PRESTADOR',
	  login,
	  logout,
	  updateUser,
	}
	}, [session, login, logout, updateUser])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)

  if (!context) {
	throw new Error('useAuth must be used within AuthProvider')
  }

  return context
}




