import { Link } from 'react-router-dom'

function CadastroCliente() {
  return (
    <div className="min-h-screen bg-[#f3f3f5] px-4 py-16">
      <div className="mx-auto max-w-lg">
        <div className="rounded-3xl bg-white p-8 shadow-sm md:p-10">
          <div className="mb-8 text-center">
            <h1 className="mb-2 text-4xl font-bold text-slate-900">
              Cadastro de Cliente
            </h1>
            <p className="text-gray-500">
              Preencha os dados abaixo para se cadastrar.
            </p>
          </div>

          <form className="space-y-5">
            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">
                Nome completo
              </label>
              <input
                type="text"
                placeholder="Digite seu nome"
                className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
              />
            </div>

            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">
                E-mail
              </label>
              <input
                type="email"
                placeholder="seuemail@exemplo.com"
                className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
              />
            </div>

            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">
                Telefone
              </label>
              <input
                type="tel"
                placeholder="(11) 99999-9999"
                className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
              />
            </div>

            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">
                Senha
              </label>
              <input
                type="password"
                placeholder="Crie uma senha"
                className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
              />
            </div>

            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">
                Confirmar senha
              </label>
              <input
                type="password"
                placeholder="Digite a senha novamente"
                className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
              />
            </div>

            <button
              type="submit"
              className="w-full rounded-2xl bg-blue-600 px-6 py-3 font-semibold text-white transition hover:bg-blue-700"
            >
              Cadastrar
            </button>
          </form>

          <div className="mt-6 text-center text-sm text-gray-600">
            Já tem conta?{' '}
            <Link
              to="/login"
              className="font-semibold text-blue-600 hover:underline"
            >
              Entrar
            </Link>
          </div>
        </div>
      </div>
    </div>
  )
}

export default CadastroCliente