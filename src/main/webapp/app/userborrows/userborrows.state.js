(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('userborrows', {
            parent: 'app',
            url: '/userborrows/{userId}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UserBorrows'
            },
            views: {
                'content@': {
                    templateUrl: 'app/userborrows/userborrows.html',
                    controller: 'UserBorrowsController',
                    controllerAs: 'vm'
                }
            },

            resolve: {
                entities: ['$stateParams', 'UserBorrows', function($stateParams, UserBorrows) {
                    return UserBorrows.get({userId : $stateParams.userId}).$promise;
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
