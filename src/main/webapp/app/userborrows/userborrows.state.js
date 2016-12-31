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
            url: '/userborrows',
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
            }
        });
    }

})();
