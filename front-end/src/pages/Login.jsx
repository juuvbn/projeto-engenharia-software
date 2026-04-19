import { Link } from 'react-router-dom'

function Login() {
  return (
    <div className="min-h-screen bg-[#f3f3f5] px-4 py-16">
      <div className="mx-auto max-w-md">
        <div className="rounded-3xl bg-white p-8 shadow-sm md:p-10">
          <div className="mb-8 text-center">
            <h1 className="mb-2 text-4xl font-bold text-slate-900">
              Bem-vindo de volta
            </h1>
            <p className="text-gray-500">
              Acesse sua conta no Cuidar+
            </p>
          </div>

          <form className="space-y-5">
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
              <div className="mb-2 flex items-center justify-between">
                <label className="block text-sm font-medium text-slate-700">
                  Senha
                </label>
              </div>

              <input
                type="password"
                placeholder="Digite sua senha"
                className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
              />
            </div>
            <button
                  type="button"
                  className="text-sm font-medium text-blue-600 hover:underline"
                >
                  Esqueci minha senha
                </button>

            <button
              type="submit"
              className="w-full rounded-2xl bg-blue-600 px-6 py-3 font-semibold text-white transition hover:bg-blue-700"
            >
              Entrar
            </button>
          </form>

          <div className="my-6 flex items-center gap-3">
            <div className="h-px flex-1 bg-gray-200"></div>
            <span className="text-xs uppercase tracking-wider text-gray-400">
              ou
            </span>
            <div className="h-px flex-1 bg-gray-200"></div>
          </div>

          <div className="space-y-3 text-center text-sm text-gray-600">
            <p>
              Não tem conta?{' '}
              <Link
                to="/cadastro-cliente"
                className="font-semibold text-blue-600 hover:underline"
              >
                Cadastrar como cliente
              </Link>
            </p>

            <p>
              Quer oferecer serviços?{' '}
              <Link
                to="/cadastro-prestador"
                className="font-semibold text-blue-600 hover:underline"
              >
                Cadastro de prestador
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Login