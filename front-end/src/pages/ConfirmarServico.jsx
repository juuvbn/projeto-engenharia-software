import React, { useState } from 'react'
import { useNavigate, useParams, useLocation } from 'react-router-dom'
import { Plus, Trash2 } from 'lucide-react'
import { aceitarProposta } from '../services/servicoService'

function emptyMaterial() {
  return { produto: '', quantidade: 1, valor: '' }
}

function toDatetimeLocal(instant) {
  if (!instant) return ''
  const d = new Date(instant)
  const pad = n => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`
}

function calcTotal(materiais) {
  return materiais.reduce((acc, m) => {
    const qty = Number(m.quantidade) || 0
    const unit = parseFloat(m.valor) || 0
    return acc + qty * unit
  }, 0)
}

function formatCurrency(value) {
  return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

export default function ConfirmarServico() {
  const { id } = useParams()
  const navigate = useNavigate()
  const location = useLocation()
  const servicoInicial = location.state?.servico ?? null

  const [materiais, setMateriais] = useState(() => {
    const m = servicoInicial?.materiais
    if (m?.length) {
      return m.map(item => ({
        produto: item.produto,
        quantidade: String(item.quantidade),
        valor: String(item.valor),
      }))
    }
    return []
  })

  const [dataHorario, setDataHorario] = useState(
    () => toDatetimeLocal(servicoInicial?.dataHorario)
  )
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const minDatetime = new Date(Date.now() - new Date().getTimezoneOffset() * 60000)
    .toISOString().slice(0, 16)

  function updateMaterial(index, field, value) {
    setMateriais(prev => prev.map((m, i) => i === index ? { ...m, [field]: value } : m))
  }

  function addMaterial() {
    setMateriais(prev => [...prev, emptyMaterial()])
  }

  function removeMaterial(index) {
    setMateriais(prev => prev.filter((_, i) => i !== index))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)

    if (!dataHorario) {
      setError('Informe a data e o horário do serviço.')
      return
    }
    if (new Date(dataHorario) <= new Date()) {
      setError('A data proposta deve ser uma data futura.')
      return
    }
    const materialInvalido = materiais.find(
      m => !m.produto.trim() || Number(m.quantidade) < 1 || parseFloat(m.valor) < 0 || m.valor === ''
    )
    if (materialInvalido) {
      setError('Preencha todos os campos de cada material corretamente.')
      return
    }

    setLoading(true)
    try {
      await aceitarProposta(id, {
        dataHorario: new Date(dataHorario).toISOString(),
        materiais: materiais.map(m => ({
          produto: m.produto.trim(),
          quantidade: Number(m.quantidade),
          valor: parseFloat(m.valor),
        })),
      })
      navigate('/meus-servicos')
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const total = calcTotal(materiais)

  return (
    <div className="min-h-screen bg-[#f8f9fa]">
      <section className="bg-white border-b border-gray-100 px-8 py-8">
        <div className="max-w-2xl mx-auto">
          <h1 className="text-3xl font-bold text-[#1a3a32]">Propor Materiais e Data</h1>
          <p className="text-gray-500 mt-1">Serviço #{id} — informe os materiais necessários e a data proposta.</p>
        </div>
      </section>

      <main className="max-w-2xl mx-auto px-8 py-8">
        <form onSubmit={handleSubmit}>
          <div className="bg-white rounded-3xl border border-gray-100 shadow-sm p-8 space-y-6">

            <div>
              <label className="text-sm font-semibold text-gray-800 block mb-2">
                Data e horário proposto
              </label>
              <input
                type="datetime-local"
                value={dataHorario}
                min={minDatetime}
                required
                onChange={e => setDataHorario(e.target.value)}
                className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:border-[#1a3a32] transition-colors"
              />
            </div>

            <div>
              <h2 className="text-sm font-semibold text-gray-800 mb-1">Materiais necessários</h2>
              <p className="text-xs text-gray-400 mb-3">Opcional — deixe vazio se o serviço não exigir materiais.</p>

              <div className="space-y-3 mb-4">
                {materiais.map((material, index) => (
                  <div key={index} className="flex gap-3 items-center">
                    <input
                      type="text"
                      placeholder="Produto (ex: Resistência de chuveiro)"
                      value={material.produto}
                      onChange={e => updateMaterial(index, 'produto', e.target.value)}
                      className="flex-1 border border-gray-200 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:border-[#1a3a32] transition-colors"
                    />
                    <input
                      type="number"
                      placeholder="Qtd"
                      min="1"
                      value={material.quantidade}
                      onChange={e => updateMaterial(index, 'quantidade', e.target.value)}
                      className="w-20 border border-gray-200 rounded-xl px-3 py-2.5 text-sm focus:outline-none focus:border-[#1a3a32] transition-colors"
                    />
                    <input
                      type="number"
                      placeholder="Valor unit."
                      min="0"
                      step="0.01"
                      value={material.valor}
                      onChange={e => updateMaterial(index, 'valor', e.target.value)}
                      className="w-28 border border-gray-200 rounded-xl px-3 py-2.5 text-sm focus:outline-none focus:border-[#1a3a32] transition-colors"
                    />
                    <button
                      type="button"
                      onClick={() => removeMaterial(index)}
                      className="p-2 text-gray-400 hover:text-red-500 transition-colors"
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>
                ))}
              </div>

              <button
                type="button"
                onClick={addMaterial}
                className="flex items-center gap-2 text-sm text-[#1a3a32] font-medium hover:underline"
              >
                <Plus size={16} />
                Adicionar material
              </button>
            </div>

            <div className="flex justify-between items-center py-4 border-t border-gray-100">
              <span className="text-sm font-semibold text-gray-700">Valor total estimado</span>
              <span className="text-lg font-bold text-[#1a3a32]">{formatCurrency(total)}</span>
            </div>

            {error && <p className="text-red-500 text-sm">{error}</p>}

            <div className="flex gap-3">
              <button
                type="button"
                onClick={() => navigate('/meus-servicos')}
                className="px-6 py-3 rounded-xl border border-gray-200 text-gray-600 font-semibold text-sm hover:bg-gray-50 transition-colors"
              >
                Cancelar
              </button>
              <button
                type="submit"
                disabled={loading}
                className="px-6 py-3 rounded-xl bg-[#1a3a32] text-white font-semibold text-sm hover:bg-[#2a4a42] disabled:opacity-50 transition-colors"
              >
                {loading ? 'Enviando...' : 'Enviar proposta'}
              </button>
            </div>
          </div>
        </form>
      </main>
    </div>
  )
}
