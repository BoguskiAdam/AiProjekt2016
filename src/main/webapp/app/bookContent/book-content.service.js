(function() {
    'use strict';
    angular
        .module('aiProjektApp')
        .factory('BorrowedBookContent', BorrowedBookContent);

    BorrowedBookContent.$inject = ['$resource'];

    function BorrowedBookContent ($resource) {
        var resourceUrl =  'api/book-contents/isbn/:isbn';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                isArray: false,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
