import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { MessageSquare, User, Calendar, DollarSign, Package, Phone, Mail, MapPin } from 'lucide-react'
import { useAuth } from '../context/AuthContext'
import { listarServicosDoPrestador } from '../services/prestadorService'
import {
  listarServicosDoCliente,
  negarProposta,
  confirmarServico,
  recusarServico,
  proporData,
} from '../services/servicoService'

// Returns true when it's the current user's turn to act, false when it's the third party's, null for terminal states
function isMyTurn(status, isCliente) {
  if (status === 'ACEITACAO_PRESTADOR_PENDENTE') return !isCliente
  if (status === 'ACEITACAO_CLIENTE_PENDENTE')   return isCliente
  return null
}

function getStatusBadgeClass(status, isCliente) {
  const turn = isMyTurn(status, isCliente)
  if (turn === true)  return 'bg-blue-100 text-blue-700'
  if (turn === false) return 'bg-amber-100 text-amber-700'
  switch (status) {
    case 'ACEITO':     return 'bg-green-100 text-green-700'
    case 'NEGADO':     return 'bg-red-100 text-red-700'
    case 'FINALIZADO': return 'bg-gray-100 text-gray-600'
    default:           return 'bg-gray-100 text-gray-600'
  }
}

function statusLabel(status, isCliente) {
  switch (status) {
    case 'ACEITACAO_PRESTADOR_PENDENTE':
      return isCliente ? 'Aguardando prestador' : 'Aguardando sua resposta'
    case 'ACEITACAO_CLIENTE_PENDENTE':
      return isCliente ? 'Aguardando sua confirmação' : 'Aguardando cliente'
    case 'ACEITO':     return 'Aceito'
    case 'NEGADO':     return 'Negado'
    case 'FINALIZADO': return 'Finalizado'
    default:           return status
  }
}

function StatusBadge({ status, isCliente }) {
  const badge = getStatusBadgeClass(status, isCliente)
  return (
    <span className={`px-2 py-0.5 rounded-full text-xs font-semibold ${badge}`}>
      {statusLabel(status, isCliente)}
    </span>
  )
}

function formatDate(instant) {
  if (!instant) return '—'
  return new Date(instant).toLocaleString('pt-BR', {
    day: '2-digit', month: '2-digit', year: 'numeric',
    hour: '2-digit', minute: '2-digit',
  })
}

function formatCurrency(value) {
  if (value == null) return 'R$ 0,00'
  return Number(value).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

function toDatetimeLocal(instant) {
  if (!instant) return ''
  const d = new Date(instant)
  const pad = n => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`
}

export default function MeusServicos() {
  const navigate = useNavigate()
  const { isCliente, isPrestador, isAuthenticated } = useAuth()

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login')
    }
  }, [isAuthenticated, navigate])
  const [servicos, setServicos] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [selectedId, setSelectedId] = useState(null)
  const [actionLoading, setActionLoading] = useState(false)
  const [actionError, setActionError] = useState(null)
  const [proposingDate, setProposingDate] = useState(false)
  const [novaData, setNovaData] = useState('')

  useEffect(() => {
    const fetch = isCliente ? listarServicosDoCliente : listarServicosDoPrestador
    fetch()
      .then(page => setServicos(page?.content ?? []))
      .catch(err => setError(err.message))
      .finally(() => setLoading(false))
  }, [isCliente])

  const selected = servicos.find(s => s.id === selectedId) ?? null
  const showPrestadorActions = isPrestador && selected?.status === 'ACEITACAO_PRESTADOR_PENDENTE'
  const showClienteActions = isCliente && selected?.status === 'ACEITACAO_CLIENTE_PENDENTE'

  const contraparteLabel = isCliente ? 'Prestador' : 'Contratante'
  const contraparte = isCliente ? selected?.contratado : selected?.contratante

  function updateServico(updated) {
    setServicos(prev => prev.map(s => s.id === updated.id ? updated : s))
  }

  async function handleAction(fn) {
    setActionLoading(true)
    setActionError(null)
    try {
      const updated = await fn()
      updateServico(updated)
      setProposingDate(false)
    } catch (err) {
      setActionError(err.message)
    } finally {
      setActionLoading(false)
    }
  }

  function handleNegar() {
    handleAction(() => negarProposta(selected.id))
  }

  function handleConfirmar() {
    handleAction(() => confirmarServico(selected.id))
  }

  function handleRecusar() {
    handleAction(() => recusarServico(selected.id))
  }

  function handleProporData(e) {
    e.preventDefault()
    handleAction(() => proporData(selected.id, new Date(novaData).toISOString()))
  }

  function openProposeDate() {
    setNovaData(toDatetimeLocal(selected.dataHorario))
    setProposingDate(true)
  }

  return (
    <div className="min-h-screen bg-[#f8f9fa]">
      <section className="bg-white border-b border-gray-100 px-8 py-8">
        <div className="max-w-7xl mx-auto">
          <h1 className="text-3xl font-bold text-[#1a3a32]">Meus Serviços</h1>
          <p className="text-gray-500 mt-1">Gerencie as propostas e serviços recebidos.</p>
        </div>
      </section>

      <main className="max-w-7xl mx-auto px-8 py-8">
        {loading && <p className="text-gray-400 text-sm">Carregando serviços...</p>}
        {error && <p className="text-red-500 text-sm">Erro ao carregar serviços: {error}</p>}

        {!loading && !error && (
          <div className="flex gap-6 h-[calc(100vh-200px)]">
            {/* Coluna esquerda — lista de cards */}
            <div className="w-80 flex-shrink-0 overflow-y-auto space-y-3 pr-1">
              {servicos.length === 0 && (
                <div className="bg-white rounded-2xl border border-gray-100 p-6 text-center">
                  <p className="text-gray-400 text-sm">Nenhum serviço encontrado.</p>
                </div>
              )}
              {servicos.map(servico => {
                const isSelected = servico.id === selectedId
                const turn = isMyTurn(servico.status, isCliente)
                const pendingClass = turn === true
                  ? 'border-blue-400 ring-2 ring-blue-200 bg-blue-50/40 hover:shadow-md'
                  : turn === false
                  ? 'border-amber-400 ring-2 ring-amber-200 bg-amber-50/40 hover:shadow-md'
                  : 'border-gray-100 bg-white hover:border-gray-300 hover:shadow-sm'
                return (
                  <button
                    key={servico.id}
                    onClick={() => { setSelectedId(servico.id); setProposingDate(false); setActionError(null) }}
                    className={`w-full text-left p-4 rounded-2xl border transition-all ${
                      isSelected
                        ? 'border-[#1a3a32] bg-[#1a3a32]/5 shadow-sm ring-0'
                        : pendingClass
                    }`}
                  >
                    <p className="text-sm font-semibold text-gray-900 line-clamp-2 mb-2">
                      {servico.descricao}
                    </p>
                    <StatusBadge status={servico.status} isCliente={isCliente} />
                    <p className="text-xs text-gray-400 mt-1">
                      Atualizado: {formatDate(servico.updateTimestamp)}
                    </p>
                  </button>
                )
              })}
            </div>

            {/* Coluna direita — detalhes */}
            <div className="flex-1 bg-white rounded-3xl border border-gray-100 shadow-sm overflow-y-auto">
              {!selected ? (
                <div className="h-full flex items-center justify-center">
                  <div className="text-center text-gray-400">
                    <MessageSquare size={40} className="mx-auto mb-3 opacity-40" />
                    <p className="text-sm">Selecione um serviço para ver os detalhes.</p>
                  </div>
                </div>
              ) : (
                <div className="p-8">
                  <div className="flex items-start justify-between mb-6">
                    <h2 className="text-xl font-bold text-gray-900 max-w-xl">{selected.descricao}</h2>
                    <StatusBadge status={selected.status} isCliente={isCliente} />
                  </div>

                  <div className="grid grid-cols-2 gap-4 mb-6">
                    <div className="flex items-center gap-3 p-4 bg-gray-50 rounded-2xl">
                      <User size={18} className="text-[#1a3a32]" />
                      <div>
                        <p className="text-xs text-gray-500">{contraparteLabel}</p>
                        <p className="text-sm font-semibold text-gray-900">{contraparte?.nome ?? '—'}</p>
                      </div>
                    </div>

                    <div className="flex items-center gap-3 p-4 bg-gray-50 rounded-2xl">
                      <Calendar size={18} className="text-[#1a3a32]" />
                      <div>
                        <p className="text-xs text-gray-500">Data / Horário</p>
                        <p className="text-sm font-semibold text-gray-900">{formatDate(selected.dataHorario)}</p>
                      </div>
                    </div>

                    <div className="flex items-center gap-3 p-4 bg-gray-50 rounded-2xl">
                      <DollarSign size={18} className="text-[#1a3a32]" />
                      <div>
                        <p className="text-xs text-gray-500">Valor total</p>
                        <p className="text-sm font-semibold text-gray-900">{formatCurrency(selected.valor)}</p>
                      </div>
                    </div>

                    <div className="flex items-center gap-3 p-4 bg-gray-50 rounded-2xl">
                      <Calendar size={18} className="text-[#1a3a32]" />
                      <div>
                        <p className="text-xs text-gray-500">Criado em</p>
                        <p className="text-sm font-semibold text-gray-900">{formatDate(selected.creationTimestamp)}</p>
                      </div>
                    </div>

                    <div className="flex items-center gap-3 p-4 bg-gray-50 rounded-2xl">
                      <Calendar size={18} className="text-[#1a3a32]" />
                      <div>
                        <p className="text-xs text-gray-500">Última atualização</p>
                        <p className="text-sm font-semibold text-gray-900">{formatDate(selected.updateTimestamp)}</p>
                      </div>
                    </div>
                  </div>

                  {selected.materiais?.length > 0 && (
                    <div className="mb-6">
                      <div className="flex items-center gap-2 mb-3">
                        <Package size={16} className="text-[#1a3a32]" />
                        <h3 className="text-sm font-semibold text-gray-700">Materiais</h3>
                      </div>
                      <div className="space-y-2">
                        {selected.materiais.map(m => (
                          <div key={m.id} className="flex justify-between text-sm p-3 bg-gray-50 rounded-xl">
                            <span className="text-gray-800">{m.produto}</span>
                            <span className="text-gray-500">{m.quantidade}× {formatCurrency(m.valor)}</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}

                  {isPrestador && selected.status === 'ACEITO' && (
                    <div className="mb-6">
                      <h3 className="text-sm font-semibold text-gray-700 mb-3">Dados de contato</h3>
                      <div className="space-y-2">
                        <div className="flex items-center gap-3 p-3 bg-gray-50 rounded-xl text-sm">
                          <Phone size={15} className="text-[#1a3a32] flex-shrink-0" />
                          <span className="text-gray-800">{selected.contratante?.telefone ?? '—'}</span>
                        </div>
                        <div className="flex items-center gap-3 p-3 bg-gray-50 rounded-xl text-sm">
                          <Mail size={15} className="text-[#1a3a32] flex-shrink-0" />
                          <span className="text-gray-800">{selected.contratante?.email ?? '—'}</span>
                        </div>
                        <div className="flex items-center gap-3 p-3 bg-gray-50 rounded-xl text-sm">
                          <MapPin size={15} className="text-[#1a3a32] flex-shrink-0" />
                          <span className="text-gray-800">{selected.contratante?.endereco ?? '—'}</span>
                        </div>
                      </div>
                    </div>
                  )}

                  {isCliente && selected.status === 'ACEITO' && (
                    <div className="mb-6">
                      <h3 className="text-sm font-semibold text-gray-700 mb-3">Dados de contato do prestador</h3>
                      <div className="space-y-2">
                        <div className="flex items-center gap-3 p-3 bg-gray-50 rounded-xl text-sm">
                          <Phone size={15} className="text-[#1a3a32] flex-shrink-0" />
                          <span className="text-gray-800">{selected.contratado?.telefone ?? '—'}</span>
                        </div>
                        <div className="flex items-center gap-3 p-3 bg-gray-50 rounded-xl text-sm">
                          <Mail size={15} className="text-[#1a3a32] flex-shrink-0" />
                          <span className="text-gray-800">{selected.contratado?.email ?? '—'}</span>
                        </div>
                        <div className="flex items-center gap-3 p-3 bg-gray-50 rounded-xl text-sm">
                          <MapPin size={15} className="text-[#1a3a32] flex-shrink-0" />
                          <span className="text-gray-800">{selected.contratado?.endereco ?? '—'}</span>
                        </div>
                      </div>
                    </div>
                  )}

                  {actionError && (
                    <p className="text-red-500 text-sm mb-4">{actionError}</p>
                  )}

                  {/* Ações do prestador */}
                  {showPrestadorActions && (
                    <div className="flex gap-3 mt-8 pt-6 border-t border-gray-100">
                      <button
                        onClick={handleNegar}
                        disabled={actionLoading}
                        className="px-6 py-3 rounded-xl border border-red-200 text-red-600 font-semibold text-sm hover:bg-red-50 disabled:opacity-50 transition-colors"
                      >
                        Negar
                      </button>
                      <button
                        onClick={() => navigate(`/meus-servicos/${selected.id}/confirmar`, { state: { servico: selected } })}
                        disabled={actionLoading}
                        className="px-6 py-3 rounded-xl bg-[#1a3a32] text-white font-semibold text-sm hover:bg-[#2a4a42] disabled:opacity-50 transition-colors"
                      >
                        Propor materiais e data
                      </button>
                    </div>
                  )}

                  {/* Ações do cliente */}
                  {showClienteActions && !proposingDate && (
                    <div className="flex gap-3 mt-8 pt-6 border-t border-gray-100">
                      <button
                        onClick={handleRecusar}
                        disabled={actionLoading}
                        className="px-6 py-3 rounded-xl border border-red-200 text-red-600 font-semibold text-sm hover:bg-red-50 disabled:opacity-50 transition-colors"
                      >
                        Recusar
                      </button>
                      <button
                        onClick={openProposeDate}
                        disabled={actionLoading}
                        className="px-6 py-3 rounded-xl border border-[#1a3a32] text-[#1a3a32] font-semibold text-sm hover:bg-[#1a3a32]/5 disabled:opacity-50 transition-colors"
                      >
                        Propor nova data
                      </button>
                      <button
                        onClick={handleConfirmar}
                        disabled={actionLoading}
                        className="px-6 py-3 rounded-xl bg-[#1a3a32] text-white font-semibold text-sm hover:bg-[#2a4a42] disabled:opacity-50 transition-colors"
                      >
                        Confirmar
                      </button>
                    </div>
                  )}

                  {/* Formulário inline de proposta de nova data */}
                  {showClienteActions && proposingDate && (
                    <form onSubmit={handleProporData} className="mt-8 pt-6 border-t border-gray-100">
                      <p className="text-sm font-semibold text-gray-700 mb-3">Propor nova data</p>
                      <div className="flex gap-3 items-center">
                        <input
                          type="datetime-local"
                          value={novaData}
                          min={new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16)}
                          onChange={e => setNovaData(e.target.value)}
                          required
                          className="flex-1 border border-gray-200 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:border-[#1a3a32] transition-colors"
                        />
                        <button
                          type="button"
                          onClick={() => setProposingDate(false)}
                          className="px-4 py-2.5 rounded-xl border border-gray-200 text-gray-600 text-sm hover:bg-gray-50 transition-colors"
                        >
                          Cancelar
                        </button>
                        <button
                          type="submit"
                          disabled={actionLoading}
                          className="px-4 py-2.5 rounded-xl bg-[#1a3a32] text-white text-sm font-semibold hover:bg-[#2a4a42] disabled:opacity-50 transition-colors"
                        >
                          Enviar
                        </button>
                      </div>
                    </form>
                  )}
                </div>
              )}
            </div>
          </div>
        )}
      </main>
    </div>
  )
}
