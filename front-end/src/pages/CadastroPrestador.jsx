import { Link } from 'react-router-dom'

function CadastroPrestador() {
  return (
    <div className="min-h-screen bg-[#f3f3f5] px-4 py-16">
      <div className="mx-auto max-w-2xl">
        <div className="rounded-3xl bg-white p-8 shadow-sm md:p-10">
          <div className="mb-8 text-center">
            <h1 className="mb-2 text-4xl font-bold text-slate-900">
              Cadastro de Prestador
            </h1>
            <p className="text-gray-500">
              Ofereça seus serviços no Cuidar+
            </p>
          </div>

          <form className="space-y-5">
            {/* Nome + telefone */}
            <div className="grid md:grid-cols-2 gap-4">
              <div>
                <label className="mb-2 block text-sm font-medium text-slate-700">
                  Nome completo
                </label>
                <input
                  type="text"
                  className="w-full rounded-2xl border border-gray-300 px-4 py-3 focus:ring-2 focus:ring-blue-200"
                />
              </div>

              <div>
                <label className="mb-2 block text-sm font-medium text-slate-700">
                  Telefone
                </label>
                <input
                  type="tel"
                  className="w-full rounded-2xl border border-gray-300 px-4 py-3 focus:ring-2 focus:ring-blue-200"
                />
              </div>
            </div>

            {/* Email */}
            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">
                E-mail
              </label>
              <input
                type="email"
                className="w-full rounded-2xl border border-gray-300 px-4 py-3 focus:ring-2 focus:ring-blue-200"
              />
            </div>

            {/* Senha */}
            <div className="grid md:grid-cols-2 gap-4">
              <div>
                <label className="mb-2 block text-sm font-medium text-slate-700">
                  Senha
                </label>
                <input
                  type="password"
                  className="w-full rounded-2xl border border-gray-300 px-4 py-3 focus:ring-2 focus:ring-blue-200"
                />
              </div>

              <div>
                <label className="mb-2 block text-sm font-medium text-slate-700">
                  Confirmar senha
                </label>
                <input
                  type="password"
                  className="w-full rounded-2xl border border-gray-300 px-4 py-3 focus:ring-2 focus:ring-blue-200"
                />
              </div>
            </div>

            {/* Especialidades */}
            <div>
              <label className="mb-3 block text-sm font-medium text-slate-700">
                Especialidades
              </label>

              <div className="grid md:grid-cols-2 gap-3">
                {[
                  'Babá',
                  'Cuidador de idosos',
                  'Cuidador infantil',
                  'Pets',
                  'Plantas',
                  'Serviços domésticos'
                ].map((item) => (
                  <label
                    key={item}
                    className="flex items-center gap-3 rounded-xl border border-gray-200 p-3 hover:bg-gray-50 cursor-pointer"
                  >
                    <input type="checkbox" />
                    <span className="text-sm">{item}</span>
                  </label>
                ))}
              </div>
            </div>

            {/* Descrição */}
            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">
                Descrição profissional
              </label>
              <textarea
                rows="4"
                placeholder="Fale sobre sua experiência"
                className="w-full rounded-2xl border border-gray-300 px-4 py-3 focus:ring-2 focus:ring-blue-200"
              />
            </div>

            {/* Botão */}
            <button
              type="submit"
              className="w-full rounded-2xl bg-blue-600 px-6 py-3 font-semibold text-white hover:bg-blue-700"
            >
              Cadastrar como prestador
            </button>
          </form>

          <div className="mt-6 text-center text-sm text-gray-600">
            Já tem conta?{' '}
            <Link to="/login" className="text-blue-600 font-semibold">
              Entrar
            </Link>
          </div>
        </div>
      </div>
    </div>
  )
}

export default CadastroPrestador