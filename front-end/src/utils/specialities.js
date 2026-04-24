export const ESPECIALIDADES_OPTIONS = [
  { value: 'BABA', label: 'Babá' },
  { value: 'CUIDADOR_DE_IDOSOS', label: 'Cuidador de idosos' },
  { value: 'CUIDADOR_DE_PETS', label: 'Cuidador de pets' },
  { value: 'ENFERMEIRO', label: 'Enfermeiro' },
]

export const ESPECIALIDADES_LABELS = ESPECIALIDADES_OPTIONS.reduce((acc, option) => {
  acc[option.value] = option.label
  return acc
}, {})

