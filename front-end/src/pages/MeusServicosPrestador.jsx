import React from 'react';

const MeusServicosPrestador = () => {
  return (
    <div className="min-h-screen bg-[#f8f9fa] font-['DM_Sans'] pb-12 text-left">
      <section className="bg-white border-b border-gray-100 px-8 py-10">
        <div className="max-w-7xl mx-auto flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div>
            <h1 className="text-4xl font-bold text-[#1a3a32] font-['Fraunces']">Painel do Prestador</h1>
            <p className="text-gray-500 mt-2">Você tem 3 novos pedidos na sua região.</p>
          </div>
          <div className="flex gap-4">
            <div className="bg-[#1a3a32]/5 px-6 py-4 rounded-2xl border border-[#1a3a32]/10">
              <p className="text-xs text-gray-500 uppercase font-bold tracking-wider">Saldo a Receber</p>
              <p className="text-xl font-bold text-[#1a3a32]">R$ 450,00</p>
            </div>
          </div>
        </div>
      </section>

      <main className="max-w-7xl mx-auto px-8 mt-10">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          <div className="space-y-6">
            <div className="bg-white p-6 rounded-3xl border border-gray-100 shadow-sm">
              <h3 className="font-bold text-gray-900 mb-4">Sua Disponibilidade</h3>
              <div className="flex items-center gap-2 text-green-600 bg-green-50 p-3 rounded-xl">
                <span className="relative flex h-3 w-3">
                  <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-green-400 opacity-75"></span>
                  <span className="relative inline-flex rounded-full h-3 w-3 bg-green-500"></span>
                </span>
                <span className="text-sm font-bold">Online para novos serviços</span>
              </div>
            </div>
          </div>

          <div className="lg:col-span-2 space-y-6">
            <h3 className="font-bold text-xl text-gray-900">Serviços Disponíveis</h3>
            
            <div className="bg-white p-8 rounded-3xl border border-gray-100 shadow-sm hover:shadow-md transition-shadow">
              <div className="flex justify-between items-start mb-6">
                <div>
                  <span className="px-3 py-1 bg-blue-50 text-blue-600 rounded-full text-xs font-bold uppercase">Pet Sitter</span>
                  <h4 className="text-2xl font-bold text-gray-900 mt-2">Passeio com Border Collie</h4>
                  <p className="text-gray-500">Solicitado por: Ana Clara • 2km de distância</p>
                </div>
                <p className="text-2xl font-bold text-[#1a3a32]">R$ 65,00</p>
              </div>
              <div className="flex gap-4">
                <button className="flex-1 bg-[#1a3a32] text-white py-4 rounded-2xl font-bold hover:opacity-90 transition-all">
                  Aceitar Serviço
                </button>
                <button className="px-8 py-4 border border-gray-200 rounded-2xl font-bold text-gray-400 hover:bg-gray-50 transition-all">
                  Ignorar
                </button>
              </div>
            </div>
          </div>

        </div>
      </main>
    </div>
  );
};

export default MeusServicosPrestador;
