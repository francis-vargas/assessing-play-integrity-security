//script utilizado para bypass das chamadas de API
Java.perform(() => {
    const MainActivity = Java.use("com.exemplo.playintegritydemo.MainActivity");
    try {
        MainActivity.solicitarTokenClassico.implementation = function () {
            console.log("Bloqueado: solicitarTokenClassico");
        };
        MainActivity.solicitarTokenPadrao.implementation = function () {
            console.log("Bloqueado: solicitarTokenPadrao");
        };
        MainActivity.solicitarTokenCombinado.implementation = function () {
            console.log("Bloqueado: solicitarTokenCombinado");
        };
        console.log("Métodos bloqueados com sucesso!");
    } catch (err) {
        console.log("Erro ao sobrescrever: " + err);
    }
});