(function () {
	"use strict";

	var REGRAS = {
		"js-altura": /^[0-9]?([,][0-9]{0,2})?$/,
		"js-email": /^[A-Za-z0-9@._%+\-]*$/,
		"js-nome": /^[A-Za-zÀ-ÿ0-9' .\-]*$/,
		"js-matricula": /^[A-Za-z0-9]*$/,
		"js-inteiro": /^[0-9]*$/
	};

	function regraDe(el) {
		if (!el || !el.classList) {
			return null;
		}
		for (var classe in REGRAS) {
			if (REGRAS.hasOwnProperty(classe) && el.classList.contains(classe)) {
				return classe;
			}
		}
		return null;
	}

	function aplicar(el) {
		var classe = regraDe(el);
		if (classe === null) {
			return;
		}

		var ultimo = el.getAttribute("data-ultimo-valor");
		if (ultimo === null) {
			ultimo = "";
		}

		if (REGRAS[classe].test(el.value)) {
			el.setAttribute("data-ultimo-valor", el.value);
			if (classe === "js-matricula") {
				el.value = el.value.toUpperCase();
				el.setAttribute("data-ultimo-valor", el.value);
			}
		}
		else {
			el.value = ultimo;
		}
	}

	document.addEventListener("input", function (evento) {
		aplicar(evento.target);
	}, true);

	document.addEventListener("keypress", function (evento) {
		var el = evento.target;
		var classe = regraDe(el);
		if (classe === null || evento.ctrlKey || evento.metaKey || evento.altKey) {
			return;
		}

		var caractere = String.fromCharCode(evento.which);
		if (!REGRAS[classe].test(el.value + caractere)) {
			evento.preventDefault();
		}
	}, true);
})();
