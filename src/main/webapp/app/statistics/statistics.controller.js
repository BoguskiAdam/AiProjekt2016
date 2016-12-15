(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('StatisticsController', StatisticsController);

    StatisticsController.$inject = ['$scope', '$state', 'ParseLinks', 'AlertService', 'Statistics'];

    function StatisticsController ($scope, $state, ParseLinks, AlertService, Statistics) {
        var vm = this;
        vm.test = 'Nie zaladowane';
        loadAll();
        console.log('loadAll - Przed');

        function loadAll () {
                    Statistics.query({
                    }, onSuccess, onError);
                    function onSuccess(data, headers) {
                        console.log('loadAll - onSuccess');
                        console.log(JSON.stringify(data));
//                        vm.links = ParseLinks.parse(headers('link'));
                        vm.totalItems = headers('X-Total-Count');
                        vm.queryCount = vm.totalItems;
                        vm.bookRatings = data;
                        console.log('loadAll - onSuccess Complete');
                    }
                    function onError(error) {
                        console.log('loadAll - onError');
                        AlertService.error(error.data.message);
                    }
            }

            loadAllStatics();
            function loadAllStatics(){
                console.log('loadAll');
                vm.test = 'zaladowane';
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.books = data;
                vm.page = pagingParams.page;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
    })();
