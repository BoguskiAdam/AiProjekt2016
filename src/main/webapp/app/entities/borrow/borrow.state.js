(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('borrow', {
            parent: 'entity',
            url: '/borrow?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Borrows'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/borrow/borrows.html',
                    controller: 'BorrowController',
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
        .state('borrow-detail', {
            parent: 'entity',
            url: '/borrow/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Borrow'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/borrow/borrow-detail.html',
                    controller: 'BorrowDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Borrow', function($stateParams, Borrow) {
                    return Borrow.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'borrow',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('borrow-detail.edit', {
            parent: 'borrow-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/borrow/borrow-dialog.html',
                    controller: 'BorrowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Borrow', function(Borrow) {
                            return Borrow.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('borrow.new', {
            parent: 'borrow',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/borrow/borrow-dialog.html',
                    controller: 'BorrowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                isbn: null,
                                borrowDate: null,
                                returnDate: null,
                                fee: null,
                                paid: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('borrow', null, { reload: 'borrow' });
                }, function() {
                    $state.go('borrow');
                });
            }]
        })
        .state('borrow.edit', {
            parent: 'borrow',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/borrow/borrow-dialog.html',
                    controller: 'BorrowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Borrow', function(Borrow) {
                            return Borrow.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('borrow', null, { reload: 'borrow' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('borrow.delete', {
            parent: 'borrow',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/borrow/borrow-delete-dialog.html',
                    controller: 'BorrowDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Borrow', function(Borrow) {
                            return Borrow.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('borrow', null, { reload: 'borrow' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
