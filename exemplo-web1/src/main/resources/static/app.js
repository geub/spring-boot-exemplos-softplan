angular.module('unjExemplo', []).controller('exemploController', function($http, $scope) {
	$scope.testarServidor = function() {
		$http.get('/testarServidor').success(function(respostaServidor) {
			$scope.respostaServidor = respostaServidor;
		});
	};
});

