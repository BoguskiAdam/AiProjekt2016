(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bookStore', {
            parent: 'app',
            url: '/bookStore?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Books'
            },
            views: {
                'content@': {
                    templateUrl: 'app/bookStore/bookStores.html',
                    controller: 'BookStoreController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('book-detail', {
            parent: 'app',
            url: '/book/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Book'
            },
            views: {
                'content@': {
                    templateUrl: 'app/bookStore/book-detail.html',
                    controller: 'BookStoreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Book', function($stateParams, Book) {
                    return Book.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'book',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        });
    }

})();
