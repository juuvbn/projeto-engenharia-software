import { useEffect, useRef, useState } from 'react'
import { ChevronDown, KeyRound, LogOut, PencilLine, UserCircle2 } from 'lucide-react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'

function UserMenu() {
  const [open, setOpen] = useState(false)
  const menuRef = useRef(null)
  const navigate = useNavigate()
  const { user, logout } = useAuth()

  useEffect(() => {
    function handleClickOutside(event) {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setOpen(false)
      }
    }

    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  function handleLogout() {
    logout()
    setOpen(false)
    navigate('/')
  }

  const initials = user?.nome
    ? user.nome
        .split(' ')
        .map((part) => part[0])
        .join('')
        .slice(0, 2)
        .toUpperCase()
    : 'US'

  return (
    <div ref={menuRef} className="relative">
      <button
        type="button"
        onClick={() => setOpen((value) => !value)}
        className="flex items-center gap-3 rounded-2xl border border-gray-200 bg-white px-4 py-3 text-left shadow-sm transition hover:border-blue-200 hover:shadow-md"
      >
        <span className="flex h-10 w-10 items-center justify-center rounded-full bg-blue-600 font-semibold text-white">
          {initials}
        </span>
        <span className="hidden min-w-0 flex-col text-sm md:flex">
          <span className="truncate font-semibold text-slate-900">{user?.nome}</span>
          <span className="truncate text-xs uppercase tracking-wide text-gray-500">
            {user?.tipoUsuario?.toLowerCase()}
          </span>
        </span>
        <ChevronDown className={`h-4 w-4 text-gray-500 transition ${open ? 'rotate-180' : ''}`} />
      </button>

      {open && (
        <div className="absolute right-0 z-50 mt-3 w-72 overflow-hidden rounded-3xl border border-gray-100 bg-white p-2 shadow-xl">
          <div className="px-4 py-3">
            <p className="text-sm font-semibold text-slate-900">{user?.nome}</p>
            <p className="text-xs text-gray-500">{user?.email}</p>
          </div>

          <div className="h-px bg-gray-100" />

          <Link
            to="/perfil"
            onClick={() => setOpen(false)}
            className="flex items-center gap-3 rounded-2xl px-4 py-3 text-sm text-slate-700 transition hover:bg-gray-50"
          >
            <UserCircle2 className="h-4 w-4 text-gray-500" />
            Meu perfil
          </Link>
          <Link
            to="/perfil/editar"
            onClick={() => setOpen(false)}
            className="flex items-center gap-3 rounded-2xl px-4 py-3 text-sm text-slate-700 transition hover:bg-gray-50"
          >
            <PencilLine className="h-4 w-4 text-gray-500" />
            Editar perfil
          </Link>
          <Link
            to="/perfil/senha"
            onClick={() => setOpen(false)}
            className="flex items-center gap-3 rounded-2xl px-4 py-3 text-sm text-slate-700 transition hover:bg-gray-50"
          >
            <KeyRound className="h-4 w-4 text-gray-500" />
            Alterar senha
          </Link>

          <div className="h-px bg-gray-100" />

          <button
            type="button"
            onClick={handleLogout}
            className="flex w-full items-center gap-3 rounded-2xl px-4 py-3 text-sm font-medium text-red-600 transition hover:bg-red-50"
          >
            <LogOut className="h-4 w-4" />
            Sair
          </button>
        </div>
      )}
    </div>
  )
}

export default UserMenu

