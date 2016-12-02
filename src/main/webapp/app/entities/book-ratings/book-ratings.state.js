(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('book-ratings', {
            parent: 'entity',
            url: '/book-ratings?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'BookRatings'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/book-ratings/book-ratings.html',
                    controller: 'BookRatingsController',
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
        .state('book-ratings-detail', {
            parent: 'entity',
            url: '/book-ratings/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BookRatings'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/book-ratings/book-ratings-detail.html',
                    controller: 'BookRatingsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'BookRatings', function($stateParams, BookRatings) {
                    return BookRatings.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'book-ratings',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('book-ratings-detail.edit', {
            parent: 'book-ratings-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book-ratings/book-ratings-dialog.html',
                    controller: 'BookRatingsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BookRatings', function(BookRatings) {
                            return BookRatings.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('book-ratings.new', {
            parent: 'book-ratings',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book-ratings/book-ratings-dialog.html',
                    controller: 'BookRatingsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                isbn: null,
                                rating: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('book-ratings', null, { reload: 'book-ratings' });
                }, function() {
                    $state.go('book-ratings');
                });
            }]
        })
        .state('book-ratings.edit', {
            parent: 'book-ratings',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book-ratings/book-ratings-dialog.html',
                    controller: 'BookRatingsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BookRatings', function(BookRatings) {
                            return BookRatings.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('book-ratings', null, { reload: 'book-ratings' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('book-ratings.delete', {
            parent: 'book-ratings',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/book-ratings/book-ratings-delete-dialog.html',
                    controller: 'BookRatingsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BookRatings', function(BookRatings) {
                            return BookRatings.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('book-ratings', null, { reload: 'book-ratings' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
