(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Book', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function HomeController ($scope, Principal, LoginService, $state, Book, ParseLinks, AlertService, pagingParams, paginationConstants) {
        var vm = this;
        //var data = Book
        //vm.tableParams = new NgTableParams({}, { dataset: data});

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

         vm.loadPage = loadPage;
                vm.predicate = pagingParams.predicate;
                vm.reverse = pagingParams.ascending;
                vm.transition = transition;
                vm.itemsPerPage = paginationConstants.itemsPerPage;

                loadAll();

                function loadAll () {
                    Book.query({
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage,
                        sort: sort()
                    }, onSuccess, onError);
                    function sort() {
                        var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                        if (vm.predicate !== 'id') {
                            result.push('id');
                        }
                        return result;
                    }
                    function onSuccess(data, headers) {
                        vm.links = ParseLinks.parse(headers('link'));
                        vm.totalItems = headers('X-Total-Count');
                        vm.queryCount = vm.totalItems;
                        //if(vm.books.avaialable !== 0)
                        vm.books = data;
                        vm.page = pagingParams.page;
                    }
                    function onError(error) {
                        AlertService.error(error.data.message);
                    }
                }

                function loadPage (page) {
                    vm.page = page;
                    vm.transition();
                }

                function transition () {
                    $state.transitionTo($state.$current, {
                        page: vm.page,
                        sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                        search: vm.currentSearch
                    });
                }
    }
})();
