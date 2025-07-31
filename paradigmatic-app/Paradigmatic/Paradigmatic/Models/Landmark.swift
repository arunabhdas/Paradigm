//
//  Landmark.swift
//  Paradigmatic
//
//  Created by Arunabh Das on 7/31/25.
//

import Foundation
import SwiftUI


struct Landmark: Hashable, Codable {
    var id: Int
    var name: String
    var park: String
    var state: String
    var description: String

    private var imageName: String
    
    var image: Image {
        Image(self.imageName)
    }
}



